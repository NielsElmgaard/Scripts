import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerPraiseAutomationWithUI extends Application
{
  private static final int[][] PLAYER_COORDINATES = {{635, 315}, {635, 370},
      {635, 425}, {635, 480}, {635, 535}, {635, 590}, {635, 645}, {635, 700},
      {635, 755}, {635, 810}, {635, 865}, {635, 920}, {635, 975}, {635, 1030},
      {635, 1085}, {635, 1140}, {635, 1195}, {635, 1250}, {635, 1305}};

  private static final int RATING_X = 1033;
  private static final int RATING_Y = 180;
  private static final int RATING_WIDTH = 48;
  private static final int RATING_HEIGHT = 25;

  private static final String tessDataPath = "C:\\Users\\niels\\IdeaProjects\\Personal\\fm24script\\tessdata";
  private static final int PRAISE_BUTTON_X = 2331;
  private static final int PRAISE_BUTTON_Y = 183;
  private static final int PRAISE_TEXT_X = 1200;
  private static final int PRAISE_TEXT_Y = 730;
  private static final int SAD_X = 1200;
  private static final int SAD_Y = 755;

  private static final Rectangle QUIT_CHAT_REGION = new Rectangle(992, 480, 572,
      677);

  private boolean running = false;
  private Thread automationThread;

  private TextArea logArea;

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override public void start(Stage primaryStage)
  {
    // Set up the UI
    logArea = new TextArea();
    logArea.setEditable(false);
    logArea.setPrefHeight(300);

    Button startButton = new Button("Start");
    Button stopButton = new Button("Stop");

    startButton.setOnAction(event -> startAutomation());
    stopButton.setOnAction(event -> stopAutomation());

    VBox vbox = new VBox(10, startButton, stopButton, logArea);
    Scene scene = new Scene(vbox, 400, 350);

    primaryStage.setTitle("Player Praise Automation");
    primaryStage.setScene(scene);
    primaryStage.show();

    // Set up global key listener
    setupGlobalKeyListener();
  }

  private void setupGlobalKeyListener()
  {
    // Disable JNativeHook logging
    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setLevel(Level.OFF);

    try
    {
      GlobalScreen.registerNativeHook();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    GlobalScreen.addNativeKeyListener(new NativeKeyListener()
    {
      @Override public void nativeKeyPressed(NativeKeyEvent e)
      {
        if (e.getKeyCode() == NativeKeyEvent.VC_SCROLL_LOCK)
        { // Start automation on 'Scroll Lock'
          Platform.runLater(() -> startAutomation());
        }
        else if (e.getKeyCode() == NativeKeyEvent.VC_PAUSE)
        { // Stop automation on 'Pause'
          Platform.runLater(() -> stopAutomation());
        }
      }

      @Override public void nativeKeyReleased(NativeKeyEvent e)
      {
      }

      @Override public void nativeKeyTyped(NativeKeyEvent e)
      {
      }
    });
  }

  private void startAutomation()
  {
//    if (running)
//      return; // Prevent multiple threads
    running = true;
    automationThread = new Thread(this::runAutomation);
    automationThread.setDaemon(true);
    automationThread.start();
    log("Automation started...");
  }

  private void stopAutomation()
  {
    running = false;
    if (automationThread != null && automationThread.isAlive())
    {
      automationThread.interrupt();
    }
    log("Automation stopped.");
  }

  private void runAutomation()
  {
    try
    {
      Robot robot = new Robot();
      ITesseract tess4j = new Tesseract();
      tess4j.setDatapath(tessDataPath);
      tess4j.setLanguage("dan");

      while (running)
      {
        for (int[] coordinates : PLAYER_COORDINATES)
        {
          if (!running)
            break;

          // Click on player
          clickOnPlayer(robot, coordinates[0], coordinates[1]);

          // Get player rating
          double rating = getPlayerRating(tess4j);
          log("Player Rating: " + rating);

          // If rating is below 7.3, stop the automation
          if (rating < 7.3)
          {
            log("Player rating below 7.3, stopping automation.");
            stopAutomation();  // Stop the automation immediately
            break;  // Break out of the for-loop to stop processing further players
          }

          // If rating is above 7.3, praise the player
          praisePlayer(robot);
          log("Praised player at: (" + coordinates[0] + ", " + coordinates[1] + ")");

          // Click on praise text
          clickOnPraiseButton(robot);

          // Quit the praise chat using OCR and dynamic coordinates
          if (quitPraiseChatUsingOCR(robot, tess4j))
          {
            log("Praise chat ended.");
          }

          // Sleep between actions
          Thread.sleep(500);
        }

        // Stop automation after looping through all players if not stopped already
        if (running)
        {
          stopAutomation();
        }
        break;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }


  private void clickOnPlayer(Robot robot, int x, int y) throws AWTException
  {
    robot.mouseMove(x, y);
    delay(100);  // Delay after moving the mouse
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    delay(200);  // Delay after clicking
  }

  private double getPlayerRating(ITesseract tess4j)
      throws AWTException, TesseractException, IOException
  {
    // Capture the rating area as an image
    Rectangle ratingRegion = new Rectangle(RATING_X, RATING_Y, RATING_WIDTH,
        RATING_HEIGHT);
    BufferedImage screenshot = new Robot().createScreenCapture(ratingRegion);
    ImageIO.write(screenshot, "png", new File("player_rating.png"));

    // Use OCR to get the rating
    String result = tess4j.doOCR(screenshot);
    try
    {
      return Double.parseDouble(result.trim());
    }
    catch (NumberFormatException e)
    {
      log("Failed to parse rating: " + result);
      return 7.3;
    }
  }

  private void praisePlayer(Robot robot) throws AWTException
  {
    robot.mouseMove(PRAISE_BUTTON_X, PRAISE_BUTTON_Y);
    delay(100);  // Delay after moving the mouse
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    delay(200);  // Delay after clicking
  }

  private void clickOnPraiseButton(Robot robot) throws AWTException
  {
    robot.mouseMove(PRAISE_TEXT_X, PRAISE_TEXT_Y);
    delay(100);  // Delay after moving the mouse
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    delay(200);  // Delay after clicking
  }

  private boolean quitPraiseChatUsingOCR(Robot robot, ITesseract tess4j)
      throws AWTException, TesseractException, IOException
  {
    delay(500);
    // Capture the region for OCR (praise chat area)
    BufferedImage screenshot = robot.createScreenCapture(QUIT_CHAT_REGION);

    ImageIO.write(screenshot, "png", new File("debug_quit_chat.png"));

    // Use Tesseract to get text from this region
    String result = tess4j.doOCR(screenshot);

    // Log the OCR result for debugging
    log("OCR result: " + result);

    // Check if the result contains keywords like "afslut" or "snak"
    if (result.toLowerCase().replaceAll("\\s+", " ").contains("afslut hurtig snak"))
    {
      // Move the mouse to the dynamically calculated center and click
      int wordCenterX = QUIT_CHAT_REGION.x + QUIT_CHAT_REGION.width / 2;
      int wordCenterY = 525;

      log("Coordinates: "+wordCenterX+", "+wordCenterY);

      robot.mouseMove(wordCenterX, wordCenterY);
      delay(100);
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      delay(200);

      return true;
    }
    if (result.toLowerCase().contains("fair") || result.toLowerCase()
        .contains("nok"))
    {
      robot.mouseMove(SAD_X, SAD_Y);
      delay(100);  // Delay after moving the mouse
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      delay(200);  // Delay after clicking

      // Move the mouse to the dynamically calculated center and click
      int wordCenterX = QUIT_CHAT_REGION.x + QUIT_CHAT_REGION.width / 2;
      int wordCenterY = QUIT_CHAT_REGION.y + QUIT_CHAT_REGION.height - (
          QUIT_CHAT_REGION.height / 3);

      robot.mouseMove(wordCenterX, wordCenterY);
      delay(100);
      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      delay(200);

      return true;
    }

    return false;
  }

  private void delay(int milliseconds)
  {
    try
    {
      Thread.sleep(milliseconds);
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
  }

  private void log(String message)
  {
    Platform.runLater(() -> logArea.appendText(message + "\n"));
  }
}
