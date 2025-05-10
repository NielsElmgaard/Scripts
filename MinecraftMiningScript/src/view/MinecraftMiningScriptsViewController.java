package view;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import viewmodel.MinecraftMiningScriptsViewModel;

public class MinecraftMiningScriptsViewController
{
  private MinecraftMiningScriptsViewModel viewModel;
  private Region root;
  private ViewHandler viewHandler;

  public MinecraftMiningScriptsViewController()
  {

  }

  public void init(ViewHandler viewHandler,
      MinecraftMiningScriptsViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void autoclickButton()
  {
    viewHandler.openView("autoclick");
  }

  @FXML private void autoFishingButton(){
    viewHandler.openView("autofishing");
  }
}
