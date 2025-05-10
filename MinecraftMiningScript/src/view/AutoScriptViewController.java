package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.AutoScriptViewModel;

public class AutoScriptViewController {
  private AutoScriptViewModel viewModel;
  private Region root;
  private ViewHandler viewHandler;

  @FXML private TextField msAutoGrind;
  @FXML private Label autoErrorLabel;

  public void init(ViewHandler viewHandler, AutoScriptViewModel viewModel, Region root) {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    // Bind AutoClicker controls
    Bindings.bindBidirectional(msAutoGrind.textProperty(), this.viewModel.autoGrindDelayProperty(), new NumberStringConverter());

    autoErrorLabel.textProperty().bind(viewModel.errorMessageProperty());

    reset(); // Clear any initial errors
  }

  public void reset() {
    viewModel.clear();
  }

  public Region getRoot() {
    return root;
  }

  @FXML private void toggleAutoClick() {
    viewModel.toggleAutoClicker();
  }

  @FXML private void toggleAutoFish() {
    viewModel.toggleAutoFishing();
  }

  @FXML private void backButton() {
    viewHandler.openView("scripts");
  }
}