package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelManager implements Model
{
  private AutoClicker autoClicker;
  private PropertyChangeSupport property;


  public ModelManager()
  {
    this.autoClicker = new AutoClicker(50);
    this.property = new PropertyChangeSupport(this);

    autoClicker.addListener("delay", evt -> property.firePropertyChange(evt));
    autoClicker.addListener("isRunning", evt -> property.firePropertyChange(evt));
  }

  @Override public void setTriggerKeyCode(int keyCode){
    autoClicker.setTriggerKeyCode(keyCode);
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
    return autoClicker.isRunning();
  }

  @Override public void startAutoClicker()
  {
    autoClicker.startAutoClicker();
  }

  @Override public void stopAutoClicker()
  {
    autoClicker.stopAutoClicker();
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