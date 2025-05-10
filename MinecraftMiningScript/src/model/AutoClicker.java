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

public class AutoClicker implements NamedPropertyChangeSubject,
    NativeKeyListener
{
  private volatile boolean isRunning = false;
  private Robot robot;
  private int delay;
  private PropertyChangeSupport property;
  private int triggerKeyCode = NativeKeyEvent.VC_SCROLL_LOCK;

  public AutoClicker(int delay)
  {
    this.property = new PropertyChangeSupport(this);
    setDelay(delay);
    try
    {
      this.robot = new Robot();
      GlobalScreen.registerNativeHook();
      GlobalScreen.addNativeKeyListener(this);
    }
    catch (AWTException e)
    {
      throw new RuntimeException(
          "Failed to initialize Robot class: " + e.getMessage(), e);
    }
    catch (NativeHookException e)
    {
      System.err.println("There was a problem registering the native hook.");
      System.err.println(e.getMessage());
    }
  }

  public void shutdownHook() {
    try {
      GlobalScreen.unregisterNativeHook();
    } catch (NativeHookException e) {
      System.err.println("Failed to unregister native hook: " + e.getMessage());
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

  private void performClick()
  {
    if (robot != null && isRunning)
    {
      System.out.println("Clicked!");
      System.out.println(isRunning);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      System.out.println("Released!");
      System.out.println(isRunning);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      System.out.println(isRunning);
      System.out.println(Thread.currentThread().getName());
    }
  }

  public void startAutoClicker()
  {
    if (!isRunning)
    {
      boolean oldValue = this.isRunning;
      isRunning = true;
      property.firePropertyChange("isRunning", oldValue, this.isRunning);
      Thread clickerThread = new Thread(() -> {
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
          property.firePropertyChange("isRunning", oldValueInterrupt,
              this.isRunning);
        }
        catch (Exception e)
        {
          boolean oldValueException = this.isRunning;
          isRunning = false;
          property.firePropertyChange("isRunning", oldValueException,
              this.isRunning);
          throw new RuntimeException(
              "Error during autoClicking: " + e.getMessage());
        }
      });
      clickerThread.setDaemon(true);
      clickerThread.start();
    }
  }

  public void stopAutoClicker()
  {
    if (isRunning)
    {
      boolean oldValue = this.isRunning;
      isRunning = false;
      property.firePropertyChange("isRunning", oldValue, this.isRunning);
      System.out.println("Stopped! ");
    }
  }

  public void setTriggerKeyCode(int triggerKeyCode)
  {
    this.triggerKeyCode = triggerKeyCode;
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent e) {
    if (e.getKeyCode() == triggerKeyCode) {
      if (isRunning) {
        stopAutoClicker();
      } else {
        startAutoClicker();
      }
    }
  }

  @Override public void nativeKeyReleased(NativeKeyEvent e) {}
  @Override public void nativeKeyTyped(NativeKeyEvent e) {}

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
