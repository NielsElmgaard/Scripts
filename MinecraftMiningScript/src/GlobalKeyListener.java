// GlobalKeyListener.java
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import model.Model;

public class GlobalKeyListener implements NativeKeyListener {
  private Model model;
  private int autoClickKey = NativeKeyEvent.VC_CAPS_LOCK;
  private int autoFishKey = NativeKeyEvent.VC_SCROLL_LOCK;

  public GlobalKeyListener(Model model) {
    this.model = model;
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent e) {
    if (e.getKeyCode() == autoClickKey) {
      if (model.isAutoGrindRunning()) {
        model.stopAutoClicker();
      } else {
        model.startAutoClicker();
      }
    } else if (e.getKeyCode() == autoFishKey) {
      if (model.isAutoFishingRunning()) {
        model.stopFishing();
      } else {
        model.startFishing();
      }
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent e) {}

  @Override
  public void nativeKeyTyped(NativeKeyEvent e) {}
}
