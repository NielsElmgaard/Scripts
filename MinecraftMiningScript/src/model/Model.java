package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

public interface Model extends NamedPropertyChangeSubject
{
  // Auto Clicker
  void setAutoClickDelay(int delay);
  int getDelay();
  boolean isRunning();
  void startAutoClicker();
  void stopAutoClicker();
  void setTriggerKeyCodeForAutoClicking(int keyCode);
  NativeKeyListener getAutoClicker();
  void setAutoClickerViewVisible(boolean visible);

  // Auto Fishing
  void setTriggerKeyCodeForAutoFishing(int keyCode);
  void startFishing();
  void stopFishing();
  NativeKeyListener getAutoFishing();
  void setAutoFishingViewVisible(boolean visible);
}
