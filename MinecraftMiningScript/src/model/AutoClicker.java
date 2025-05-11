package model;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AutoClicker
    implements NamedPropertyChangeSubject, NativeKeyListener
{
  private volatile boolean isRunning = false;
  private Robot robot;
  private int delay;
  private PropertyChangeSupport property;
  private int triggerKeyCode = NativeKeyEvent.VC_CAPS_LOCK;
  private boolean isListenerActive = false;
  private Thread grinderThread;

  public AutoClicker(int delay)
  {
    this.property = new PropertyChangeSupport(this);
    this.grinderThread = new Thread();
    setDelay(delay);
    try
    {
      this.robot = new Robot();
    }
    catch (AWTException e)
    {
      throw new RuntimeException(
          "Failed to initialize Robot class: " + e.getMessage(), e);
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
        System.out.println("AutoClicker key listener registered.");
      }
      catch (Exception e)
      {
        System.err.println(
            "Error registering AutoClicker key listener: " + e.getMessage());
      }
    }
  }

  public void unregisterKeyListener() throws NativeHookException
  {
    if (isListenerActive)
    {
      GlobalScreen.removeNativeKeyListener(this);
      isListenerActive = false;
      System.out.println("AutoClicker key listener unregistered.");
    }
  }

  public int getDelay()
  {
    return delay;
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  public void setDelay(int delay)
  {
    int oldValue = this.delay;
    this.delay = delay;
    property.firePropertyChange("delay", oldValue, this.delay);
  }

  private void performClick() throws InterruptedException
  {
    if (robot != null && isRunning)
    {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
  }

  public void startAutoClicker()
  {
    if (!isRunning)
    {
      boolean oldValue = this.isRunning;
      isRunning = true;
      property.firePropertyChange("isAutoGrindRunning", oldValue,
          this.isRunning);

      this.grinderThread = new Thread(() -> {
        try
        {
          while (isRunning)
          {
            performClick();
            Thread.sleep(this.delay);
            System.out.println("sleep: " + this.delay);
          }
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
          boolean oldValueInterrupt = this.isRunning;
          isRunning = false;
          property.firePropertyChange("isAutoGrindRunning", oldValueInterrupt,
              this.isRunning);
        }
        catch (Exception e)
        {
          boolean oldValueException = this.isRunning;
          isRunning = false;
          property.firePropertyChange("isAutoGrindRunning", oldValueException,
              this.isRunning);
          throw new RuntimeException(
              "Error during autoClicking: " + e.getMessage());
        }
      });
      this.grinderThread.setDaemon(true);
      this.grinderThread.start();
    }
  }

  public void stopAutoClicker()
  {
    if (isRunning)
    {
      boolean oldValue = this.isRunning;
      isRunning = false;
      property.firePropertyChange("isAutoGrindRunning", oldValue,
          this.isRunning);
      System.out.println("Stopped! ");
    }
    if (grinderThread != null && grinderThread.isAlive())
    {
      grinderThread.interrupt();
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
        stopAutoClicker();
      }
      else
      {
        startAutoClicker();
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
