package model;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AutoMine implements NamedPropertyChangeSubject, NativeKeyListener
{
  public static final Rectangle MINING_REGION_DEFAULT = new Rectangle(2159,
      1000, 400, 439);
  public static final Rectangle MINING_REGION_1080P = new Rectangle(1619, 750,
      300, 329);
  private Rectangle currentMiningRegion = MINING_REGION_DEFAULT;

  private volatile boolean isRunning;
  private Robot robot;
  private Thread miningThread;
  private ITesseract tesseract;
  private int triggerKeyCode = NativeKeyEvent.VC_CAPS_LOCK;
  private PropertyChangeSupport property;
  private boolean isListenerActive = false;
  private boolean isTriggerKeyPressed = false;
  private int turnAmount;

  public AutoMine(int turnAmount)
  {
    this.property = new PropertyChangeSupport(this);
    this.isRunning = false;
    this.miningThread = new Thread();
    setTurnAmount(turnAmount);
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

  public Rectangle getCurrentMiningRegion()
  {
    return currentMiningRegion;
  }

  public void setCurrentMiningRegion(Rectangle currentMiningRegion)
  {
    Rectangle oldValue = this.currentMiningRegion;
    this.currentMiningRegion = currentMiningRegion;
    property.firePropertyChange("miningRegionChanged", oldValue,
        this.currentMiningRegion);
    System.out.println("Mining region updated: " + currentMiningRegion);
  }

  public int getTurnAmount()
  {
    return turnAmount;
  }

  public void setTurnAmount(int turnAmount)
  {
    if (turnAmount >= 0)
    {
      int oldValue = this.turnAmount;
      this.turnAmount = turnAmount;
      property.firePropertyChange("turnAmount", oldValue, this.turnAmount);
    }
  }

  public void registerKeyListener()
  {
    if (!isListenerActive)
    {
      try
      {
        GlobalScreen.addNativeKeyListener(this);
        isListenerActive = true;
        System.out.println("AutoMining key listener registered.");
      }
      catch (Exception e)
      {
        System.err.println(
            "Error registering AutoMining key listener: " + e.getMessage());
      }
    }
  }

  public void unregisterKeyListener() throws NativeHookException
  {
    if (isListenerActive)
    {
      GlobalScreen.removeNativeKeyListener(this);
      isListenerActive = false;
      System.out.println("AutoMining key listener unregistered.");
    }
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  public void startMining()
  {
    if (!isRunning)
    {
      boolean oldValue = this.isRunning;
      this.isRunning = true;
      property.firePropertyChange("isAutoMiningRunning", oldValue,
          this.isRunning);
      this.miningThread = new Thread(this::runMining);
      this.miningThread.setDaemon(true);
      this.miningThread.start();
    }
  }

  public void stopMining()
  {
    if (isRunning)
    {
      boolean oldValue = this.isRunning;
      this.isRunning = false;
      property.firePropertyChange("isAutoMiningRunning", oldValue,
          this.isRunning);
      System.out.println("Stopped Auto Mining...");
      if (miningThread != null && miningThread.isAlive())
      {
        miningThread.interrupt();
      }
    }
    else
    {
      System.out.println("Auto Mining is not running.");
    }
  }

  private void runMining()
  {
    try
    {
      while (isRunning && !Thread.currentThread().isInterrupted())
      {
        robot.keyPress(KeyEvent.VK_W);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        while (isRunning && !Thread.currentThread().isInterrupted())
        {
          BufferedImage screenshot = robot.createScreenCapture(
              currentMiningRegion);

          String result = "";
          try
          {
            result = tesseract.doOCR(screenshot).toUpperCase();
            System.out.println("OCR Result: \"" + result + "\"");
          }
          catch (TesseractException e)
          {
            System.err.println("Tesseract OCR error: " + e.getMessage());
            break;
          }
          if (!(result.contains("BROKEN") || result.contains("BRAOKEN")))
          {
            break;
          }
          Thread.sleep(50);
        }
        robot.keyRelease(KeyEvent.VK_W);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        if (!isRunning)
        {
          break;
        }

        property.firePropertyChange("miningWall", false, true);
        System.out.println("Wall detected! Turning 180...");
        Thread.sleep(200);
        turn180();
        Thread.sleep(200);
      }
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();

      System.err.println("Mining thread interrupted.");
    }
    finally
    {
      try
      {
        robot.keyRelease(KeyEvent.VK_W);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      }
      catch (Exception ignored)
      {
      }
      boolean oldValue = this.isRunning;
      isRunning = false;
      property.firePropertyChange("isAutoMiningRunning", oldValue, false);
    }
  }

  private void turn180()
  {
    try
    {
      Point currentMouse = MouseInfo.getPointerInfo().getLocation();
      int currentX = currentMouse.x;
      int currentY = currentMouse.y;

      int steps = 10;
      int delay = 10;

      robot.keyRelease(KeyEvent.VK_W);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      Thread.sleep(50);

      robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);

      for (int i = 0; i < steps; i++)
      {
        int newX = currentX + (turnAmount * i / steps);
        robot.mouseMove(newX, currentY);
        Thread.sleep(delay);
      }

      robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);

      robot.mouseMove(currentX, currentY);

      System.out.println("Performed a turn");
      Thread.sleep(50);
      for (int i = 0; i < 5; i++)
      {
        robot.keyPress(KeyEvent.VK_W);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1000);
      }
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      System.err.println("Turn interrupted");
    }
  }

  public void setTriggerKeyCode(int triggerKeyCode)
  {
    this.triggerKeyCode = triggerKeyCode;
  }

  @Override public void nativeKeyPressed(NativeKeyEvent e)
  {
    if (e.getKeyCode() == triggerKeyCode && !isTriggerKeyPressed)
    {
      isTriggerKeyPressed = true;
      if (isRunning)
      {
        stopMining();
      }
      else
      {
        startMining();
      }
    }
  }

  @Override public void nativeKeyReleased(NativeKeyEvent e)
  {
    if (e.getKeyCode() == triggerKeyCode)
    {
      isTriggerKeyPressed = false;
    }
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
