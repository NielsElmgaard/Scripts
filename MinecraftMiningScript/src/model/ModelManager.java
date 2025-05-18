package model;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelManager implements Model
{
  private AutoClicker autoClicker;
  private AutoFishing autoFishing;
  private AutoMine autoMine;
  private PropertyChangeSupport property;
  private boolean isAutoFishingViewActive = false;
  private boolean isAutoGrinderViewActive = false;
  private boolean isAutoMiningViewActive = false;
  private boolean isGlobalHookRegistered = false;

  public ModelManager()
  {
    this.autoClicker = new AutoClicker(50);
    this.autoFishing = new AutoFishing();
    this.autoMine = new AutoMine(1000,7000);
    this.property = new PropertyChangeSupport(this);

    autoClicker.addListener("delay", evt -> property.firePropertyChange(evt));
    autoClicker.addListener("isAutoGrindRunning",
        evt -> property.firePropertyChange(evt));

    autoFishing.addListener("isAutoFishingRunning",
        evt -> property.firePropertyChange(evt));
    autoFishing.addListener("fishCaught",
        evt -> property.firePropertyChange(evt));
    autoFishing.addListener("error", evt -> property.firePropertyChange(evt));
    autoFishing.addListener("ocrResult",evt -> property.firePropertyChange(evt));

    autoMine.addListener("isAutoMiningRunning",
        evt -> property.firePropertyChange(evt));
    autoMine.addListener("miningTurn", evt -> property.firePropertyChange(evt));
    autoMine.addListener("error", evt -> property.firePropertyChange(evt));
    autoMine.addListener("miningRegionChanged",
        evt -> property.firePropertyChange(evt));
    autoMine.addListener("turnAmount",evt -> property.firePropertyChange(evt));

    try
    {
      GlobalScreen.registerNativeHook();
      isGlobalHookRegistered = true;
      System.out.println("Global hook registered on startup.");
    }
    catch (NativeHookException e)
    {
      System.err.println(
          "Error registering global hook on startup: " + e.getMessage());
    }
  }

  @Override public void setAutoFishingViewActive(boolean isActive)
      throws NativeHookException
  {
    if (isActive)
    {
      autoFishing.registerKeyListener();
      isAutoFishingViewActive = true;
      System.out.println(
          "AutoFishing key listener registered (via AutoFishing method).");
    }
    else
    {
      autoFishing.unregisterKeyListener();
      isAutoFishingViewActive = false;
      System.out.println(
          "AutoFishing key listener unregistered (via AutoFishing method).");
    }
  }

  @Override public int getTurnAmount()
  {
    return autoMine.getTurnAmount();
  }

  @Override public void setTurnAmount(int turnAmount)
  {
    autoMine.setTurnAmount(turnAmount);
  }

  @Override public void setMiningDurationMilliseconds(
      int miningDurationMilliseconds)
  {
    autoMine.setMiningDurationMilliseconds(miningDurationMilliseconds);
  }

  @Override public int getMiningDurationMilliseconds()
  {
    return autoMine.getMiningDurationMilliseconds();
  }

  @Override public boolean isAutoMiningRunning()
  {
    return autoMine.isRunning();
  }

  @Override public void setTriggerKeyCodeForAutoMining(int keyCode)
  {
    autoMine.setTriggerKeyCode(keyCode);
  }

  @Override public void startMining()
  {
    autoMine.startMining();
  }

  @Override public void stopMining()
  {
    autoMine.stopMining();
  }

  @Override public NativeKeyListener getAutoMining()
  {
    return autoMine;
  }

  @Override public Rectangle getCurrentMiningRegion()
  {
    return autoMine.getCurrentMiningRegion();
  }

  @Override public void setMiningRegion(Rectangle region)
  {
    autoMine.setCurrentMiningRegion(region);
  }

  @Override public void setAutoMiningViewActive(boolean isActive)
      throws NativeHookException
  {
    if (isActive)
    {
      autoMine.registerKeyListener();
      isAutoMiningViewActive = true;
      System.out.println(
          "AutoMining key listener registered (via AutoMine method).");
    }
    else
    {
      autoMine.unregisterKeyListener();
      isAutoMiningViewActive = false;
      System.out.println(
          "AutoMining key listener unregistered (via AutoMine method).");
    }
  }

  @Override public void setAutoGrinderViewActive(boolean isActive)
      throws NativeHookException
  {
    if (isActive)
    {
      autoClicker.registerKeyListener();
      isAutoGrinderViewActive = true;
      System.out.println(
          "AutoGrinder key listener registered (via AutoGrind method).");
    }
    else
    {
      autoClicker.unregisterKeyListener();
      isAutoGrinderViewActive = false;
      System.out.println(
          "AutoGrinder key listener unregistered (via AutoGrind method).");
    }
  }

  private void manageGlobalHook()
  {
    boolean shouldBeRegistered =
        isAutoFishingViewActive || isAutoGrinderViewActive
            || isAutoMiningViewActive;
    if (shouldBeRegistered && !isGlobalHookRegistered)
    {
      try
      {
        GlobalScreen.registerNativeHook();
        isGlobalHookRegistered = true;
        System.out.println("Global hook registered (listener active).");
      }
      catch (NativeHookException e)
      {
        System.err.println("Error registering global hook: " + e.getMessage());
      }
    }
    else if (!shouldBeRegistered && isGlobalHookRegistered)
    {
      try
      {
        GlobalScreen.unregisterNativeHook();
        isGlobalHookRegistered = false;
        System.out.println("Global hook unregistered (no active listeners).");
      }
      catch (NativeHookException e)
      {
        System.err.println(
            "Error unregistering global hook: " + e.getMessage());
      }
    }
  }

  @Override public void setTriggerKeyCodeForAutoClicking(int keyCode)
  {
    autoClicker.setTriggerKeyCode(keyCode);
  }

  @Override public NativeKeyListener getAutoClicker()
  {
    return autoClicker;
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

  @Override public NativeKeyListener getAutoFishing()
  {
    return autoFishing;
  }

  @Override public Rectangle getCurrentFishingRegion()
  {
    return autoFishing.getCurrentFishingRegion();
  }

  @Override public void setFishingRegion(Rectangle region)
  {
    autoFishing.setCurrentFishingRegion(region);
  }

  @Override public void setAutoClickDelay(int delay)
  {
    autoClicker.setDelay(delay);
  }

  @Override public int getDelay()
  {
    return autoClicker.getDelay();
  }

  @Override public boolean isAutoFishingRunning()
  {
    return autoFishing.isRunning();
  }

  @Override public boolean isAutoGrindRunning()
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
    property.addPropertyChangeListener(propertyName, listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName, listener);
  }
}