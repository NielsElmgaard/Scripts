package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import model.AutoMine;
import viewmodel.AutoMineViewModel;

import java.awt.*;

public class AutoMineViewController
{
  private AutoMineViewModel viewModel;
  private Region root;
  private ViewHandler viewHandler;
  @FXML private Label errorLabel;
  @FXML private RadioButton defaultRegionRadio;
  @FXML private RadioButton p1080RegionRadio;
  @FXML private TextField turnAmount;

  public AutoMineViewController()
  {

  }

  public void init(ViewHandler viewHandler, AutoMineViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    Bindings.bindBidirectional(turnAmount.textProperty(),
        this.viewModel.turnAmountProperty(), new NumberStringConverter());

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());

    Rectangle currentRegion = viewModel.currentMiningRegionProperty().get();
    if (currentRegion.equals(AutoMine.MINING_REGION_DEFAULT))
    {
      defaultRegionRadio.setSelected(true);
    }
    else if (currentRegion.equals(AutoMine.MINING_REGION_1080P))
    {
      p1080RegionRadio.setSelected(true);
    }
    defaultRegionRadio.setOnAction(event -> viewModel.setDefaultRegion());
    p1080RegionRadio.setOnAction(event -> viewModel.set1080pRegion());

    viewModel.setViewActive(true);
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void autoMineButton()
  {
    viewModel.toggleAutoMining();
  }

  @FXML private void backButton()
  {
    viewModel.setViewActive(false);
    viewHandler.openView("scripts");
  }

}
