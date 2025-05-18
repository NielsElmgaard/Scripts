package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import viewmodel.AutoFishingViewModel;

import java.awt.*;

public class AutoFishingViewController
{
  private AutoFishingViewModel viewModel;
  private Region root;
  private ViewHandler viewHandler;
  @FXML private Label errorLabel;
  @FXML private RadioButton defaultRegionRadio;
  @FXML private RadioButton p1080RegionRadio;
  @FXML private TextArea logArea;

  public AutoFishingViewController()
  {

  }

  public void init(ViewHandler viewHandler, AutoFishingViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());

    viewModel.ocrResultLogProperty().addListener((obs, oldVal, newVal) -> {
      logArea.setText(newVal);
      logArea.positionCaret(logArea.getLength());
    });

    Rectangle currentRegion = viewModel.currentFishingRegionProperty().get();
    if (currentRegion.equals(model.AutoFishing.FISHING_REGION_DEFAULT))
    {
      defaultRegionRadio.setSelected(true);
    }
    else if (currentRegion.equals(model.AutoFishing.FISHING_REGION_1080P))
    {
      p1080RegionRadio.setSelected(true);
    }
    defaultRegionRadio.setOnAction(event -> viewModel.setDefaultRegion());
    p1080RegionRadio.setOnAction(event -> viewModel.set1080pRegion());
    System.out.println(
        "Fishing key active in init? " + viewModel.isViewActiveProperty());

    viewModel.setViewActive(true);

    System.out.println(
        "Fishing key active in init? " + viewModel.isViewActiveProperty());

  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void autoFishButton()
  {
    viewModel.toggleAutoFishing();
  }

  @FXML private void backButton()
  {
    System.out.println(
        "Fishing key active in back? " + viewModel.isViewActiveProperty());
    viewModel.setViewActive(false);
    System.out.println(
        "Fishing key active in back? " + viewModel.isViewActiveProperty());
    viewHandler.openView("scripts");
  }

}
