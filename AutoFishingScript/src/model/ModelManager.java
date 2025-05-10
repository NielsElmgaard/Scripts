package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelManager implements Model
{
  private AutoFishing autoFishing;
  private PropertyChangeSupport property;

  public ModelManager()
  {
    this.autoFishing = new AutoFishing();
    this.property = new PropertyChangeSupport(this);

    autoFishing.addListener("isRunning",
        evt -> property.firePropertyChange(evt));
    autoFishing.addListener("fishCaught",
        evt -> property.firePropertyChange(evt));
    autoFishing.addListener("error", evt -> property.firePropertyChange(evt));
  }

  @Override public void setTriggerKeyCodeForAutoFishing(int keyCode)
  {
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

  @Override public Rectangle getCurrentFishingRegion()
  {
    return autoFishing.getCurrentFishingRegion();
  }

  public void setFishingRegion(Rectangle region) {
    Rectangle oldValue = autoFishing.getCurrentFishingRegion();
    autoFishing.setCurrentFishingRegion(region);
    property.firePropertyChange("fishingRegionChanged", oldValue, autoFishing.getCurrentFishingRegion());
  }

  @Override public boolean isAutoFishingRunning()
  {
    return autoFishing.isRunning();
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