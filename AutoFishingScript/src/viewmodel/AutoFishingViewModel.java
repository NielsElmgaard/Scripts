package viewmodel;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Platform;
import javafx.beans.property.*;
import model.AutoFishing;
import model.Model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AutoFishingViewModel implements PropertyChangeListener
{
  private Model model;
  private StringProperty errorMessage;
  private BooleanProperty autoFishRunning;
  private IntegerProperty triggerKeyCode;
  private ObjectProperty<Rectangle> currentFishingRegion;

  public AutoFishingViewModel(Model model)
  {
    this.model = model;
    this.autoFishRunning = new SimpleBooleanProperty(
        model.isAutoFishingRunning());
    this.triggerKeyCode = new SimpleIntegerProperty(
        NativeKeyEvent.VC_SCROLL_LOCK);
    this.currentFishingRegion = new SimpleObjectProperty<>(
        model.getCurrentFishingRegion());

    this.errorMessage = new SimpleStringProperty(
        "Running: " + autoFishRunning.get());

    this.triggerKeyCode.addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        model.setTriggerKeyCodeForAutoFishing(newValue.intValue());
      }
    });

    model.addListener("isRunning", this);
    model.addListener("fishCaught", this);
    model.addListener("error", this);
    model.addListener("fishingRegionChanged", this);
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

  public ObjectProperty<Rectangle> currentFishingRegionProperty() {
    return currentFishingRegion;
  }

  public void setDefaultRegion() {
    model.setFishingRegion(AutoFishing.FISHING_REGION_DEFAULT);
  }

  public void set1080pRegion() {
    model.setFishingRegion(AutoFishing.FISHING_REGION_1080P);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {

      case "isRunning":
        Platform.runLater(() -> {
          autoFishRunning.set((Boolean) evt.getNewValue());
          errorMessage.set("Running: " + autoFishRunning.get());
        });
        break;
      case "fishCaught":
        Platform.runLater(() -> {
          errorMessage.set("Fish caught!");
        });
        break;
      case "error":
        Platform.runLater(() -> {
          errorMessage.set("Error: " + evt.getNewValue());
        });
        break;
      case "fishingRegionChanged":
        Platform.runLater(() -> {
          currentFishingRegion.set((Rectangle) evt.getNewValue());
        });
        break;
    }
    System.out.println(
        "ViewModel received property change: " + evt.getPropertyName() + " - "
            + evt.getNewValue());
  }
}
