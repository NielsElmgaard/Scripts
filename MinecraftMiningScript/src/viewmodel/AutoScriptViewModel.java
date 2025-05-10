package viewmodel;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.beans.property.*;
import model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AutoScriptViewModel implements PropertyChangeListener
{
  private Model model;
  private StringProperty errorMessage;

  // AutoGrind
  private IntegerProperty autoGrindDelay;
  private BooleanProperty autoGrindRunning;

  // AutoFish
  private BooleanProperty autoFishingRunning;

  public AutoScriptViewModel(Model model)
  {
    this.model = model;

    // AutoGrind
    this.autoGrindDelay = new SimpleIntegerProperty(model.getDelay());
    this.autoGrindRunning = new SimpleBooleanProperty(model.isAutoGrindRunning());
    this.errorMessage=new SimpleStringProperty("");

    this.autoGrindDelay.addListener((observable, oldValue, newValue) -> {
      if (newValue != null && newValue.intValue() >= 0)
      {
        model.setAutoClickDelay(newValue.intValue());
      }
      else
      {
        errorMessage.set("Invalid delay value.");
        autoGrindDelay.set(oldValue.intValue());
      }
    });

    // AutoFish
    this.autoFishingRunning = new SimpleBooleanProperty(model.isAutoFishingRunning());

    model.addListener("isAutoGrindRunning", this);
    model.addListener("isAutoFishingRunning", this);
    model.addListener("fishCaught", this);
    model.addListener("error", this);
  }

  // AutoGrind
  public BooleanProperty autoGrindRunningProperty()
  {
    return autoGrindRunning;
  }

  public IntegerProperty autoGrindDelayProperty()
  {
    return autoGrindDelay;
  }

  public StringProperty errorMessageProperty()
  {
    return errorMessage;
  }

  // AutoFish

  public BooleanProperty autoFishingRunningProperty()
  {
    return autoFishingRunning;
  }


  public void toggleAutoFishing()
  {
    if (autoFishingRunning.get())
    {
      model.stopFishing();
    }
    else
    {
      model.startFishing();
    }
  }

  public void toggleAutoClicker()
  {
    if (autoGrindRunning.get())
    {
      model.stopAutoClicker();
    }
    else
    {
      model.startAutoClicker();
    }
  }

  public void clear()
  {
    errorMessage.set("");
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "delay":
        autoGrindDelay.set((Integer) evt.getNewValue());
        break;
      case "isAutoGrindRunning":
        autoGrindRunning.set((Boolean) evt.getNewValue());
        break;
      case "isAutoFishingRunning":
        autoFishingRunning.set((Boolean) evt.getNewValue());
        break;
      case "fishCaught":
        errorMessage.set("Fish Caught!");
        break;
      case "error":
        errorMessage.set("Error: " + evt.getNewValue());
        break;
    }
    System.out.println(
        "CombinedViewModel received property change: " + evt.getPropertyName()
            + " - " + evt.getNewValue());
  }
}
