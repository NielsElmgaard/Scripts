package model;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

public class AutoFishing
    implements NamedPropertyChangeSubject, NativeKeyListener
{

  private static final String FISHING_SUBTITLE = "Block";
  public static final Rectangle FISHING_REGION_DEFAULT = new Rectangle(2159,
      1000, 400, 439);
  public static final Rectangle FISHING_REGION_1080P = new Rectangle(1419, 750,
      500, 329);
  private Rectangle currentFishingRegion = FISHING_REGION_DEFAULT;

  private boolean isRunning;
  private Robot robot;
  private Thread fishingThread;
  private ITesseract tesseract;
  private int triggerKeyCode = NativeKeyEvent.VC_CAPS_LOCK;
  private PropertyChangeSupport property;
  private boolean isListenerActive = false;

  public AutoFishing()
  {
    this.property = new PropertyChangeSupport(this);
    this.isRunning = false;
    this.fishingThread = new Thread();
    try
    {
      this.robot = new Robot();
    }
    catch (AWTException e)
    {
      throw new RuntimeException(
          "Failed to initialize Robot: " + e.getMessage(), e);
    }
    this.tesseract = new Tesseract();
    tesseract.setDatapath(
        "C:/Users/niels/IdeaProjects/Scripts/MinecraftMiningScript/tessdata");
    tesseract.setLanguage("eng");

  }

  public Rectangle getCurrentFishingRegion()
  {
    return currentFishingRegion;
  }

  public void setCurrentFishingRegion(Rectangle currentFishingRegion)
  {
    Rectangle oldValue = this.currentFishingRegion;
    this.currentFishingRegion = currentFishingRegion;
    property.firePropertyChange("fishingRegionChanged", oldValue,
        this.currentFishingRegion);
    System.out.println("Fishing region updated: " + currentFishingRegion);
  }

  public void registerKeyListener()
  {
    if (!isListenerActive)
    {
      try
      {
        GlobalScreen.addNativeKeyListener(this);
        isListenerActive = true;
        System.out.println("AutoFishing key listener registered.");
      }
      catch (Exception e)
      {
        System.err.println(
            "Error registering AutoFishing key listener: " + e.getMessage());
      }
    }
  }

  public void unregisterKeyListener() throws NativeHookException
  {
    if (isListenerActive)
    {
      GlobalScreen.removeNativeKeyListener(this);
      isListenerActive = false;
      System.out.println("AutoFishing key listener unregistered.");
    }
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  public void startFishing()
  {
    if (!isRunning)
    {
      boolean oldValue = this.isRunning;
      this.isRunning = true;
      property.firePropertyChange("isAutoFishingRunning", oldValue,
          this.isRunning);
      this.fishingThread = new Thread(this::runFishing);
      this.fishingThread.setDaemon(true);
      this.fishingThread.start();
    }
  }

  public void stopFishing()
  {
    if (isRunning)
    {
      boolean oldValue = this.isRunning;
      this.isRunning = false;
      property.firePropertyChange("isAutoFishingRunning", oldValue,
          this.isRunning);
      if (fishingThread != null && fishingThread.isAlive())
      {
        fishingThread.interrupt();
      }
    }
  }

  private void runFishing()
  {
    try
    {
      robot.mousePress(InputEvent.BUTTON3_MASK);
      robot.mouseRelease(InputEvent.BUTTON3_MASK);
      System.out.println("Initial right-click performed.");
      Thread.sleep(500);

      while (isRunning)
      {
        BufferedImage screenshot = robot.createScreenCapture(
            currentFishingRegion);

        File outputfile = new File(System.getProperty("java.io.tmpdir"),
            "autoFish.png");
        ImageIO.write(screenshot, "png", outputfile);

        String result = "";
        try
        {
          result = tesseract.doOCR(screenshot);
          System.out.println("OCR Result: \"" + result.trim() + "\"");
          property.firePropertyChange("ocrResult", null, result.trim());
        }
        catch (TesseractException e)
        {
          System.err.println("Tesseract OCR error: " + e.getMessage());
          boolean oldValue = this.isRunning;
          isRunning = false;
          property.firePropertyChange("isAutoFishingRunning", oldValue,
              this.isRunning);
          break;
        }
        if (result.contains(FISHING_SUBTITLE))
        {
          System.out.println("Fish detected! Performing actions.");
          Thread.sleep(randomDelay(200, 400));
          robot.mousePress(InputEvent.BUTTON3_MASK);
          robot.mouseRelease(InputEvent.BUTTON3_MASK);

          Thread.sleep(randomDelay(2600, 3000));
          robot.mousePress(InputEvent.BUTTON3_MASK);
          robot.mouseRelease(InputEvent.BUTTON3_MASK);
          property.firePropertyChange("fishCaught", false, true);
        }
        Thread.sleep(randomDelay(300, 600));
      }
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      boolean oldValue = this.isRunning;
      isRunning = false;
      property.firePropertyChange("isAutoFishingRunning", oldValue,
          this.isRunning);
      System.err.println("Fishing thread interrupted.");
    }
    catch (IOException e)
    {
      isRunning = false;
      property.firePropertyChange("error", null, e.getMessage());
    }
  }

  private int randomDelay(int minDelay, int maxDelay)
  {
    int min = Math.min(minDelay, maxDelay);
    int max = Math.max(minDelay, maxDelay);
    return (int) (min + (Math.random() * (max - min)));
  }

  public void setTriggerKeyCode(int triggerKeyCode)
  {
    this.triggerKeyCode = triggerKeyCode;
  }

  @Override public void nativeKeyPressed(NativeKeyEvent e)
  {
    if (e.getKeyCode() == triggerKeyCode)
    {
      if (isRunning)
      {
        stopFishing();
      }
      else
      {
        startFishing();
      }
    }
  }

  @Override public void nativeKeyReleased(NativeKeyEvent e)
  {
  }

  @Override public void nativeKeyTyped(NativeKeyEvent e)
  {
  }

  @Override public void addListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName, listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName, listener);
  }
}

