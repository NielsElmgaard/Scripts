package viewmodel;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.keyboard.*;
import javafx.beans.property.*;
import model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AutoClickViewModel implements PropertyChangeListener
{
  private Model model;
  private IntegerProperty autoClickDelay;
  private StringProperty errorMessage;
  private BooleanProperty autoClickRunning;
  private IntegerProperty triggerKeyCode;

  public AutoClickViewModel(Model model)
  {
    this.model = model;
    this.autoClickDelay = new SimpleIntegerProperty(model.getDelay());
    this.autoClickRunning = new SimpleBooleanProperty(model.isRunning());
    this.triggerKeyCode = new SimpleIntegerProperty(
        NativeKeyEvent.VC_CAPS_LOCK);

    this.errorMessage = new SimpleStringProperty("");
    this.autoClickDelay.addListener((observable, oldValue, newValue) -> {
      if (newValue != null && newValue.intValue() >= 0)
      {
        model.setAutoClickDelay(newValue.intValue());
      }
      else
      {
        errorMessage.set("Invalid delay value.");
        autoClickDelay.set(oldValue.intValue());
      }
    });

    this.triggerKeyCode.addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        model.setTriggerKeyCodeForAutoClicking(newValue.intValue());
      }
    });

    model.addListener("delay", this);
    model.addListener("isRunning", this);
  }

  public void clear()
  {
    autoClickDelay.set(0);
    errorMessage.set("");
    autoClickRunning.set(false);
  }

  public void toggleAutoClicker()
  {
    if (autoClickRunning.get())
    {
      model.stopAutoClicker();
    }
    else
    {
      model.startAutoClicker();
    }
  }

  public IntegerProperty getAutoClickDelayProperty()
  {
    return autoClickDelay;
  }

  public StringProperty errorMessageProperty()
  {
    return errorMessage;
  }

  public BooleanProperty isRunning()
  {
    return autoClickRunning;
  }

  public IntegerProperty triggerKeyCodeProperty() {
    return triggerKeyCode;
  }

  public void setAutoClickerViewVisible(boolean visible) {
    model.setAutoClickerViewVisible(visible);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "delay":
        autoClickDelay.set((Integer) evt.getNewValue());
        break;
      case "isRunning":
        autoClickRunning.set((Boolean) evt.getNewValue());
        break;
    }
    System.out.println(
        "ViewModel received property change: " + evt.getPropertyName() + " - "
            + evt.getNewValue());
  }
}

