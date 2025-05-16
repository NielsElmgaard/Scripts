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
  private int miningDurationMilliseconds;

  public AutoMine(int turnAmount, int miningDurationMilliseconds)
  {
    this.property = new PropertyChangeSupport(this);
    this.isRunning = false;
    this.miningThread = new Thread();
    setTurnAmount(turnAmount);
    setMiningDurationMilliseconds(miningDurationMilliseconds);
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

  public int getMiningDurationMilliseconds()
  {
    return miningDurationMilliseconds;
  }

  public void setMiningDurationMilliseconds(int miningDurationMilliseconds)
  {
    this.miningDurationMilliseconds = miningDurationMilliseconds;
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
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

        long startTime = System.currentTimeMillis();

        robot.keyPress(KeyEvent.VK_W);
        robot.keyPress(KeyEvent.VK_D);

        while (isRunning && !Thread.currentThread().isInterrupted() && (
            System.currentTimeMillis() - startTime
                < miningDurationMilliseconds))
        {
          Thread.sleep(100);
        }
        robot.keyRelease(KeyEvent.VK_W);

        robot.keyPress(KeyEvent.VK_S);
        startTime = System.currentTimeMillis();
        while (isRunning && !Thread.currentThread().isInterrupted() && (
            System.currentTimeMillis() - startTime
                < miningDurationMilliseconds))
        {
          Thread.sleep(100);
        }
        robot.keyRelease(KeyEvent.VK_D);

        robot.keyPress(KeyEvent.VK_A);
        startTime = System.currentTimeMillis();
        while (isRunning && !Thread.currentThread().isInterrupted() && (
            System.currentTimeMillis() - startTime
                < miningDurationMilliseconds))
        {
          Thread.sleep(100);
        }
        robot.keyRelease(KeyEvent.VK_S);

        robot.keyPress(KeyEvent.VK_W);
        startTime = System.currentTimeMillis();
        while (isRunning && !Thread.currentThread().isInterrupted() && (
            System.currentTimeMillis() - startTime
                < miningDurationMilliseconds))
        {
          Thread.sleep(100);
        }
        robot.keyRelease(KeyEvent.VK_A);

        robot.keyRelease(KeyEvent.VK_W);

        if (!isRunning)
        {
          break;
        }

        int oldValue = this.miningDurationMilliseconds;
        property.firePropertyChange("miningTurn", oldValue,
            this.miningDurationMilliseconds);
        System.out.println("Mining for " + miningDurationMilliseconds / 1000
            + " seconds. Turning 180...");
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
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_D);
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
      int centerX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
      int centerY = Toolkit.getDefaultToolkit().getScreenSize().height / 2;

      double sweepAmount = getTurnAmount();
      int steps = 10;
      int delay = 40;

      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyRelease(KeyEvent.VK_W);
      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      Thread.sleep(50);

      robot.mouseMove(centerX, centerY);
      Thread.sleep(delay);
      Point pivotPoint = new Point(centerX, centerY);

      robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);

      for (int i = 1; i <= steps; i++)
      {
        int deltaX = (int) (sweepAmount * Math.sin(Math.PI * i / steps));
        robot.mouseMove(pivotPoint.x + deltaX, pivotPoint.y);
        Thread.sleep(delay);
      }

      robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);

      robot.mouseMove(currentMouse.x, currentMouse.y);
      System.out.println("Performed a turn");
      Thread.sleep(100);

      //      robot.keyPress(KeyEvent.VK_W);
      //      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      //      Thread.sleep(1500);
      //      robot.keyRelease(KeyEvent.VK_W);
      //      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
      //      Thread.sleep(100);
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      System.err.println("Turn interrupted.");
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
