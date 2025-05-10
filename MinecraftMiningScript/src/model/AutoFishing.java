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

  private static final String FISHING_SUBTITLE = "Note Block plays";
  private static final Rectangle FISHING_REGION = new Rectangle(2159, 1187, 400,
      252);
  private boolean isRunning;
  private Robot robot;
  private Thread fishingThread;
  private ITesseract tesseract;
  private int triggerKeyCode = NativeKeyEvent.VC_SCROLL_LOCK;
  private PropertyChangeSupport property;

  public AutoFishing()
  {
    this.property = new PropertyChangeSupport(this);
    this.isRunning = false;
    this.fishingThread = new Thread();
    try
    {
      this.robot = new Robot();
      GlobalScreen.registerNativeHook();
      GlobalScreen.addNativeKeyListener(this);
    }
    catch (AWTException e)
    {
      throw new RuntimeException(
          "Failed to initialize Robot: " + e.getMessage(), e);
    }
    catch (NativeHookException e)
    {
      System.err.println("There was a problem registering the native hook.");
      System.err.println(e.getMessage());
    }
    this.tesseract = new Tesseract();
    tesseract.setDatapath(
        "C:/Users/niels/IdeaProjects/Scripts/MinecraftMiningScript/tessdata");
    tesseract.setLanguage("eng");

  }

  public void shutdownHook()
  {
    try
    {
      GlobalScreen.unregisterNativeHook();
    }
    catch (NativeHookException e)
    {
      System.err.println("Failed to unregister native hook: " + e.getMessage());
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
      property.firePropertyChange("isRunning", oldValue, this.isRunning);
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
      property.firePropertyChange("isRunning", oldValue, this.isRunning);
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
      while (isRunning)
      {
        // 1. Capture the fishing region.
        BufferedImage screenshot = robot.createScreenCapture(FISHING_REGION);

        File outputfile = new File("saved.png");
        ImageIO.write(screenshot, "png", outputfile);

        // 2. Perform OCR on the captured image.
        String result = "";
        try
        {
          result = tesseract.doOCR(screenshot);
        }
        catch (TesseractException e)
        {
          System.err.println("Tesseract OCR error: " + e.getMessage());
          boolean oldValue = this.isRunning;
          isRunning = false;
          property.firePropertyChange("isRunning", oldValue, this.isRunning);
          break;
        }

        // 3. Check for the fishing subtitle.
        if (result.contains(FISHING_SUBTITLE))
        {
          System.out.println("Subtitle detected! Performing actions.");
          // 4. Right-click.
          robot.mousePress(InputEvent.BUTTON3_MASK);
          robot.mouseRelease(InputEvent.BUTTON3_MASK);

          // 5. Wait for a delay (adjust as needed).
          Thread.sleep(2000);

          // 6. Right-click again.
          robot.mousePress(InputEvent.BUTTON3_MASK);
          robot.mouseRelease(InputEvent.BUTTON3_MASK);
          property.firePropertyChange("fishCaught", false, true);
        }
        Thread.sleep(500);
      }
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      boolean oldValue = this.isRunning;
      isRunning = false;
      property.firePropertyChange("isRunning", oldValue, this.isRunning);
      System.err.println("Fishing thread interrupted.");
    }
    catch (IOException e)
    {
      isRunning = false;
      property.firePropertyChange("error", null, e.getMessage());
    }
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

