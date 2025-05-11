package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import viewmodel.ViewModelFactory;

public class ViewHandler
{
  private ViewModelFactory viewModelFactory;
  private Stage primaryStage;
  private Scene currentScene;
  private MinecraftMiningScriptsViewController minecraftMiningScriptsViewController;
  private AutoClickViewController autoclickViewController;
  private AutoFishingViewController autoFishingViewController;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    this.currentScene = new Scene(new Region());
    openView("scripts");
  }

  public void openView(String id)
  {
    Region root = null;
    switch (id)
    {
      case "scripts":
        root = loadMinecraftMiningScriptsView("MinecraftMiningScriptsView.fxml");
        break;
      case "autoclick":
        root = loadAutoclickView("AutoGrindView.fxml");
        break;
      case "autofishing":
        root = loadAutoFishingView("AutoFishingView.fxml");
        break;
    }
    currentScene.setRoot(root);

    String title = "";
    if (root.getUserData() != null)
    {
      title += root.getUserData();
    }
    primaryStage.setTitle(title);
    primaryStage.setScene(currentScene);
    primaryStage.setWidth(root.getPrefWidth());
    primaryStage.setHeight(root.getPrefHeight());
    primaryStage.show();
  }

  private Region loadMinecraftMiningScriptsView(String fxmlFile)
  {
    if (minecraftMiningScriptsViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        Region root = loader.load();
        minecraftMiningScriptsViewController = loader.getController();
        minecraftMiningScriptsViewController
            .init(this, viewModelFactory.getMinecraftMiningScriptsViewModel(), root);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      minecraftMiningScriptsViewController.reset();
    }
    return minecraftMiningScriptsViewController.getRoot();
  }

  private Region loadAutoclickView(String fxmlFile)
  {
    if (autoclickViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        Region root = loader.load();
        autoclickViewController = loader.getController();
        autoclickViewController
            .init(this, viewModelFactory.getAutoclickViewModel(), root);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      autoclickViewController.reset();
    }
    return autoclickViewController.getRoot();
  }

  private Region loadAutoFishingView(String fxmlFile)
  {
    if (autoFishingViewController == null)
    {
      try
      {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        Region root = loader.load();
        autoFishingViewController = loader.getController();
        autoFishingViewController
            .init(this, viewModelFactory.getAutoFishingViewModel(), root);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      autoFishingViewController.reset();
    }
    return autoFishingViewController.getRoot();
  }
}
