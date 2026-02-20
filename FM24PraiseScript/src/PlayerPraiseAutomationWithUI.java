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
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerPraiseAutomationWithUI extends Application
    implements NativeKeyListener
{

  private static final String TESS_DATA_PATH = "C:/Users/niels/IdeaProjects/Personal/fm24script/tessdata";

  // Coordinates for each player on screen
  private static final int[][] PLAYER_LIST = {{635, 315}, {635, 370},
      {635, 425}, {635, 480}, {635, 535}, {635, 590}, {635, 645}, {635, 700},
      {635, 755}, {635, 810}, {635, 865}, {635, 920}, {635, 975}, {635, 1030},
      {635, 1085}, {635, 1140}, {635, 1195}, {635, 1250}, {635, 1305}};

  private static final Rectangle RATING_AREA = new Rectangle(1033, 180, 48, 25);
  private static final Rectangle CHAT_EXIT_AREA = new Rectangle(992, 480, 572,
      677);

  private static final Point PRAISE_MENU_BTN = new Point(2331, 183);
  private static final Point PRAISE_OPTION_BTN = new Point(1200, 730);
  private static final Point DISMISS_BTN = new Point(1200, 755);

  private final Tesseract tesseract = new Tesseract();
  private Robot robot;
  private volatile boolean isRunning = false;
  private TextArea logOutput;

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override public void start(Stage stage) throws Exception
  {
    this.robot = new Robot();
    initializeTesseract();
    initUI(stage);
    initGlobalHotkeys();
  }

  private void initializeTesseract()
  {
    tesseract.setDatapath(TESS_DATA_PATH);
    tesseract.setLanguage("dan");
  }

  private void initUI(Stage stage)
  {
    logOutput = new TextArea();
    logOutput.setEditable(false);

    Button startBtn = new Button("Start Automation");
    Button stopBtn = new Button("Stop");

    startBtn.setOnAction(e -> toggleAutomation(true));
    stopBtn.setOnAction(e -> toggleAutomation(false));

    VBox layout = new VBox(10, startBtn, stopBtn, logOutput);
    stage.setScene(new Scene(layout, 400, 400));
    stage.setTitle("FM24 Praise Automator");
    stage.show();
  }

  private void initGlobalHotkeys()
  {
    Logger.getLogger(GlobalScreen.class.getPackage().getName())
        .setLevel(Level.OFF);
    try
    {
      GlobalScreen.registerNativeHook();
      GlobalScreen.addNativeKeyListener(this);
    }
    catch (Exception e)
    {
      writeLog("Error registering hotkeys: " + e.getMessage());
    }
  }

  private synchronized void toggleAutomation(boolean start)
  {
    if (start && !isRunning)
    {
      isRunning = true;
      new Thread(this::processPlayers).start();
      writeLog("Automation started.");
    }
    else
    {
      isRunning = false;
      writeLog("Automation stopped.");
    }
  }

  private void processPlayers()
  {
    for (int[] pos : PLAYER_LIST)
    {
      if (!isRunning)
        break;

      try
      {
        performClick(pos[0], pos[1]);
        double rating = captureAndReadRating();
        writeLog("Rating found: " + rating);

        if (rating < 7.3)
        {
          writeLog("Rating too low. Aborting...");
          break;
        }

        executePraiseSequence();
        handleChatWindow();

        Thread.sleep(600);
      }
      catch (Exception e)
      {
        writeLog("Error while processing player rating: " + e.getMessage());
      }
    }
    isRunning = false;
  }

  private double captureAndReadRating() throws TesseractException
  {
    BufferedImage img = robot.createScreenCapture(RATING_AREA);
    String text = tesseract.doOCR(img).trim().replace(",", ".");
    try
    {
      return Double.parseDouble(text);
    }
    catch (NumberFormatException e)
    {
      return 0.0;
    }
  }

  private void executePraiseSequence()
  {
    performClick(PRAISE_MENU_BTN.x, PRAISE_MENU_BTN.y);
    executionDelay(200);
    performClick(PRAISE_OPTION_BTN.x, PRAISE_OPTION_BTN.y);
  }

  private void handleChatWindow() throws TesseractException
  {
    executionDelay(500);
    BufferedImage chatImg = robot.createScreenCapture(CHAT_EXIT_AREA);
    String content = tesseract.doOCR(chatImg).toLowerCase();

    if (content.contains("afslut") || content.contains("snak"))
    {
      int x = CHAT_EXIT_AREA.x + (CHAT_EXIT_AREA.width / 2);
      performClick(x, 525);
    }
    else if (content.contains("fair") || content.contains("nok"))
    {
      performClick(DISMISS_BTN.x, DISMISS_BTN.y);
      executionDelay(200);
      int x = CHAT_EXIT_AREA.x + (CHAT_EXIT_AREA.width / 2);
      performClick(x, CHAT_EXIT_AREA.y + (CHAT_EXIT_AREA.height / 2));
    }
  }

  private void performClick(int x, int y)
  {
    robot.mouseMove(x, y);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    executionDelay(150);
  }

  private void executionDelay(int ms)
  {
    try
    {
      Thread.sleep(ms);
    }
    catch (InterruptedException ignored)
    {
    }
  }

  private void writeLog(String msg)
  {
    Platform.runLater(() -> logOutput.appendText(msg + "\n"));
  }

  @Override public void nativeKeyPressed(NativeKeyEvent e)
  {
    if (e.getKeyCode() == NativeKeyEvent.VC_SCROLL_LOCK)
      Platform.runLater(() -> toggleAutomation(true));
    if (e.getKeyCode() == NativeKeyEvent.VC_PAUSE)
      Platform.runLater(() -> toggleAutomation(false));
  }

  @Override public void nativeKeyReleased(NativeKeyEvent e)
  {
  }

  @Override public void nativeKeyTyped(NativeKeyEvent e)
  {
  }
}