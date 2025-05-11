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
  private BooleanProperty isViewActive;

  public AutoClickViewModel(Model model)
  {
    this.model = model;
    this.autoClickDelay = new SimpleIntegerProperty(model.getDelay());
    this.autoClickRunning = new SimpleBooleanProperty(model.isAutoGrindRunning());
    this.triggerKeyCode = new SimpleIntegerProperty(
        NativeKeyEvent.VC_CAPS_LOCK);

    this.errorMessage = new SimpleStringProperty("");
    this.isViewActive=new SimpleBooleanProperty(false);


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

    isViewActive.addListener((observable, oldValue, newValue) -> {
      try
      {
        model.setAutoGrinderViewActive(newValue);
      }
      catch (NativeHookException e)
      {
        System.err.println("Error unregistering AutoGrinding key listener: " + e.getMessage());
      }
    });

    model.addListener("delay", this);
    model.addListener("isAutoGrindRunning", this);
  }

  public BooleanProperty isViewActiveProperty() {
    return isViewActive;
  }

  public void setViewActive(boolean active) {
    isViewActive.set(active);
  }

  public void clear()
  {
    autoClickDelay.set(model.getDelay());
    errorMessage.set("");
    autoClickRunning.set(false);
    setViewActive(true);
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


  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "delay":
        autoClickDelay.set((Integer) evt.getNewValue());
        break;
      case "isAutoGrindRunning":
        autoClickRunning.set((Boolean) evt.getNewValue());
        break;
    }
    System.out.println(
        "ViewModel received property change: " + evt.getPropertyName() + " - "
            + evt.getNewValue());
  }
}

