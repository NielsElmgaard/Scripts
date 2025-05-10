package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelManager implements Model
{
  private AutoClicker autoClicker;
  private AutoFishing autoFishing;
  private PropertyChangeSupport property;
  private boolean isAutoClickerViewVisible = false;
  private boolean isAutoFishingViewVisible = false;


  public ModelManager()
  {
    this.autoClicker = new AutoClicker(50);
    this.autoFishing=new AutoFishing();
    this.property = new PropertyChangeSupport(this);

    autoClicker.addListener("delay", evt -> property.firePropertyChange(evt));
    autoClicker.addListener("isRunning", evt -> property.firePropertyChange(evt));

    autoFishing.addListener("isRunning", evt -> property.firePropertyChange(evt));
    autoFishing.addListener("fishCaught", evt -> property.firePropertyChange(evt));
    autoFishing.addListener("error", evt -> property.firePropertyChange(evt));
  }

  @Override public void setTriggerKeyCodeForAutoClicking(int keyCode){
    autoClicker.setTriggerKeyCode(keyCode);
  }

  @Override public NativeKeyListener getAutoClicker()
  {
    return autoClicker;
  }

  @Override public void setTriggerKeyCodeForAutoFishing(int keyCode){
    autoFishing.setTriggerKeyCode(keyCode);
  }


  @Override public void startFishing()
  {
    autoFishing.startFishing();
  }

  @Override public void stopFishing()
  {
    autoFishing.stopFishing();
  }

  @Override public NativeKeyListener getAutoFishing()
  {
    return autoFishing;
  }

  @Override public void setAutoClickDelay(int delay)
  {
    autoClicker.setDelay(delay);
  }

  @Override public int getDelay()
  {
    return autoClicker.getDelay();
  }

  @Override public boolean isRunning()
  {
    return autoClicker.isRunning() || autoFishing.isRunning();
  }

  @Override public void startAutoClicker()
  {
    autoClicker.startAutoClicker();
  }

  @Override public void stopAutoClicker()
  {
    autoClicker.stopAutoClicker();
  }

  @Override public void setAutoClickerViewVisible(boolean visible) {
    isAutoClickerViewVisible = visible;
    if (visible) {
      autoClicker.enable();
    } else {
      autoClicker.disable();
    }
  }

  @Override public void setAutoFishingViewVisible(boolean visible) {
    isAutoFishingViewVisible = visible;
    if (visible) {
      autoFishing.enable();
    } else {
      autoFishing.disable();
    }
  }

  @Override public void addListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName,listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName,listener);
  }
}