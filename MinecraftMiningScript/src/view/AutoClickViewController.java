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
  @FXML private Label autoErrorLabel;

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

    autoErrorLabel.textProperty().bind(viewModel.errorMessageProperty());

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

  @FXML private void toggleAutoClick()
  {
    viewModel.toggleAutoClicker();
  }

  @FXML private void backButton()
  {
    viewModel.setViewActive(false);
    viewHandler.openView("scripts");
  }
}
