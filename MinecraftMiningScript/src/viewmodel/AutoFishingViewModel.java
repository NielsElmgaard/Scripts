package viewmodel;

import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Platform;
import javafx.beans.property.*;
import model.AutoFishing;
import model.Model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutoFishingViewModel implements PropertyChangeListener
{
  private Model model;
  private StringProperty errorMessage;
  private BooleanProperty autoFishRunning;
  private IntegerProperty triggerKeyCode;
  private ObjectProperty<Rectangle> currentFishingRegion;
  private BooleanProperty isViewActive;
  private StringProperty ocrResultLog;

  public AutoFishingViewModel(Model model)
  {
    this.model = model;
    this.autoFishRunning = new SimpleBooleanProperty(
        model.isAutoFishingRunning());
    this.triggerKeyCode = new SimpleIntegerProperty(
        NativeKeyEvent.VC_CAPS_LOCK);
    this.currentFishingRegion = new SimpleObjectProperty<>(
        model.getCurrentFishingRegion());

    this.isViewActive=new SimpleBooleanProperty(false);
    this.errorMessage = new SimpleStringProperty(
        "Running: " + autoFishRunning.get());
    this.ocrResultLog = new SimpleStringProperty("");

    this.triggerKeyCode.addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        model.setTriggerKeyCodeForAutoFishing(newValue.intValue());
      }
    });

    isViewActive.addListener((observable, oldValue, newValue) -> {
      try
      {
        model.setAutoFishingViewActive(newValue);
      }
      catch (NativeHookException e)
      {
        System.err.println("Error unregistering AutoFishing key listener: " + e.getMessage());
      }
    });

    model.addListener("isAutoFishingRunning", this);
    model.addListener("fishCaught", this);
    model.addListener("error", this);
    model.addListener("fishingRegionChanged", this);
    model.addListener("ocrResult", this);
  }

  private void logOcrResult(String result) {
    Platform.runLater(() -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
      String timestamp = LocalDateTime.now().format(formatter);
      ocrResultLog.set(ocrResultLog.get() + "[" + timestamp + "] OCR Result: \"" + result.trim() + "\"\n");
    });
  }

  public StringProperty ocrResultLogProperty()
  {
    return ocrResultLog;
  }

  public BooleanProperty isViewActiveProperty() {
    return isViewActive;
  }

  public void setViewActive(boolean active) {
    isViewActive.set(active);
  }

  public void clear()
  {
    errorMessage.set("");
    autoFishRunning.set(false);
    setViewActive(true);
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

      case "isAutoFishingRunning":
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
      case "ocrResult":
        Platform.runLater(() -> {
          if (evt.getNewValue() != null) {
            logOcrResult(evt.getNewValue().toString());
          }
        });
        break;
    }
    System.out.println(
        "ViewModel received property change: " + evt.getPropertyName() + " - "
            + evt.getNewValue());
  }
}
