package viewmodel;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.beans.property.*;
import model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AutoFishingViewModel implements PropertyChangeListener
{
  private Model model;
  private StringProperty errorMessage;
  private BooleanProperty autoFishRunning;
  private IntegerProperty triggerKeyCode;

  public AutoFishingViewModel(Model model)
  {
    this.model = model;
    this.autoFishRunning = new SimpleBooleanProperty(model.isRunning());
    this.triggerKeyCode = new SimpleIntegerProperty(
        NativeKeyEvent.VC_SCROLL_LOCK);

    this.errorMessage = new SimpleStringProperty("");

    this.triggerKeyCode.addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        model.setTriggerKeyCodeForAutoFishing(newValue.intValue());
      }
    });

    model.addListener("isRunning", this);
    model.addListener("fishCaught", this);
    model.addListener("error", this);
  }

  public void clear()
  {
    errorMessage.set("");
    autoFishRunning.set(false);
  }

  public void toggleAutoFishing()
  {
    if (autoFishRunning.get())
    {
      model.stopFishing();
    }
    else
    {
      model.startFishing();
    }
  }

  public IntegerProperty triggerKeyCodeProperty()
  {
    return triggerKeyCode;
  }

  public StringProperty errorMessageProperty()
  {
    return errorMessage;
  }

  public BooleanProperty autoFishRunningProperty()
  {
    return autoFishRunning;
  }


  public void setAutoFishingViewVisible(boolean visible) {
    model.setAutoFishingViewVisible(visible);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {

      case "isRunning":
        autoFishRunning.set((Boolean) evt.getNewValue());
        break;
      case "fishCaught":
        errorMessage.set("Fish caught!");
        break;
      case "error":
        errorMessage.set("Error: " + evt.getNewValue());
        break;
    }
    System.out.println(
        "ViewModel received property change: " + evt.getPropertyName() + " - "
            + evt.getNewValue());
  }
}
