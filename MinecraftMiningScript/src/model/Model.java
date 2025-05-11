package model;

import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.awt.*;

public interface Model extends NamedPropertyChangeSubject
{
  // Auto Clicker
  void setAutoClickDelay(int delay);
  int getDelay();
  boolean isAutoGrindRunning();
  void startAutoClicker();
  void stopAutoClicker();
  void setTriggerKeyCodeForAutoClicking(int keyCode);
  NativeKeyListener getAutoClicker();
  void setAutoGrinderViewActive(boolean isActive) throws NativeHookException;

  // Auto Fishing
  boolean isAutoFishingRunning();
  void setTriggerKeyCodeForAutoFishing(int keyCode);
  void startFishing();
  void stopFishing();
  NativeKeyListener getAutoFishing();
  Rectangle getCurrentFishingRegion();
  void setFishingRegion(Rectangle region);
  void setAutoFishingViewActive(boolean isActive) throws NativeHookException;
}
