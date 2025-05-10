package model;

import utility.observer.javaobserver.NamedPropertyChangeSubject;

public interface Model extends NamedPropertyChangeSubject
{
  // Auto Clicker
  void setAutoClickDelay(int delay);
  int getDelay();
  boolean isRunning();
  void startAutoClicker();
  void stopAutoClicker();
  void setTriggerKeyCode(int keyCode);

  // Auto Fishing

  void startFishing();
  void stopFishing();
}
