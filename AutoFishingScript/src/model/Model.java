package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.awt.*;

public interface Model extends NamedPropertyChangeSubject
{

  // Auto Fishing
  boolean isAutoFishingRunning();
  void setTriggerKeyCodeForAutoFishing(int keyCode);
  void startFishing();
  void stopFishing();
  Rectangle getCurrentFishingRegion();
  void setFishingRegion(Rectangle region);
}
