package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelManager implements Model
{
  private AutoClicker autoClicker;
  private AutoFishing autoFishing;
  private PropertyChangeSupport property;


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

  @Override public void setTriggerKeyCode(int keyCode){
    autoClicker.setTriggerKeyCode(keyCode);
//    autoFishing.setTriggerKeyCode(keyCode);
  }

  @Override public void startFishing()
  {
    autoFishing.startFishing();
  }

  @Override public void stopFishing()
  {
    autoFishing.stopFishing();
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