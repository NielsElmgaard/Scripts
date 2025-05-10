package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.AutoClickViewModel;
import viewmodel.AutoFishingViewModel;

public class AutoFishingViewController
{
  private AutoFishingViewModel viewModel;
  private Region root;
  private ViewHandler viewHandler;
  @FXML private Label errorLabel;


  public void init(ViewHandler viewHandler, AutoFishingViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    viewModel.setAutoFishingViewVisible(true);
  }

  public void reset()
  {
    viewModel.clear();
    viewModel.setAutoFishingViewVisible(false);
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
    viewHandler.openView("scripts");
  }

}
