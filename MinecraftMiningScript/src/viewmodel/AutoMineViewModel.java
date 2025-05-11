package viewmodel;

import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Platform;
import javafx.beans.property.*;
import model.AutoMine;
import model.Model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AutoMineViewModel implements PropertyChangeListener
{
  private Model model;
  private StringProperty errorMessage;
  private BooleanProperty autoMineRunning;
  private IntegerProperty triggerKeyCode;
  private ObjectProperty<Rectangle> currentMiningRegion;
  private BooleanProperty isViewActive;

  public AutoMineViewModel(Model model)
  {
    this.model = model;
    this.autoMineRunning = new SimpleBooleanProperty(
        model.isAutoMiningRunning());
    this.triggerKeyCode = new SimpleIntegerProperty(
        NativeKeyEvent.VC_CAPS_LOCK);
    this.currentMiningRegion = new SimpleObjectProperty<>(
        model.getCurrentMiningRegion());

    this.isViewActive=new SimpleBooleanProperty(false);
    this.errorMessage = new SimpleStringProperty(
        "Running: " + autoMineRunning.get());

    this.triggerKeyCode.addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        model.setTriggerKeyCodeForAutoMining(newValue.intValue());
      }
    });

    isViewActive.addListener((observable, oldValue, newValue) -> {
      try
      {
        model.setAutoMiningViewActive(newValue);
      }
      catch (NativeHookException e)
      {
        System.err.println("Error unregistering AutoMining key listener: " + e.getMessage());
      }
    });

    model.addListener("isAutoMiningRunning", this);
    model.addListener("miningWall", this);
    model.addListener("error", this);
    model.addListener("miningRegionChanged", this);
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
    autoMineRunning.set(false);
    setViewActive(true);
  }

  public void toggleAutoMining()
  {
    if (autoMineRunning.get())
    {
      model.stopMining();
    }
    else
    {
      model.startMining();
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

  public BooleanProperty autoMiningRunningProperty()
  {
    return autoMineRunning;
  }

  public ObjectProperty<Rectangle> currentMiningRegionProperty() {
    return currentMiningRegion;
  }

  public void setDefaultRegion() {
    model.setMiningRegion(AutoMine.MINING_REGION_DEFAULT);
  }

  public void set1080pRegion() {
    model.setMiningRegion(AutoMine.MINING_REGION_1080P);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {

      case "isAutoMiningRunning":
        Platform.runLater(() -> {
          autoMineRunning.set((Boolean) evt.getNewValue());
          errorMessage.set("Running: " + autoMineRunning.get());
        });
        break;
      case "miningWall":
        Platform.runLater(() -> {
          errorMessage.set("wall hit!");
        });
        break;
      case "error":
        Platform.runLater(() -> {
          errorMessage.set("Error: " + evt.getNewValue());
        });
        break;
      case "miningRegionChanged":
        Platform.runLater(() -> {
          currentMiningRegion.set((Rectangle) evt.getNewValue());
        });
        break;
    }
    System.out.println(
        "ViewModel received property change: " + evt.getPropertyName() + " - "
            + evt.getNewValue());
  }
}
