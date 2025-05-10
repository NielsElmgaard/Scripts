package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import javafx.util.converter.NumberStringConverter;
import viewmodel.AutoClickViewModel;

public class AutoClickViewController
{
  private AutoClickViewModel viewModel;
  private Region root;
  private ViewHandler viewHandler;

  @FXML private TextField msAutoGrind;
  @FXML private TextField msAutoMine;
  @FXML private Label errorLabel;

  public AutoClickViewController()
  {
  }

  public void init(ViewHandler viewHandler, AutoClickViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    Bindings.bindBidirectional(msAutoGrind.textProperty(),
        this.viewModel.getAutoClickDelayProperty(), new NumberStringConverter());

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());

  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void autoGrindButton()
  {
    viewModel.toggleAutoClicker();
  }

  @FXML private void autoMineButton()
  {
    // TO-DO
  }

  @FXML private void backButton()
  {
    viewHandler.openView("scripts");
  }
}
