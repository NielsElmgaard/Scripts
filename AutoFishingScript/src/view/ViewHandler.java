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

  private AutoFishingViewController autoFishingViewController;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    this.currentScene = new Scene(new Region());
    openView("autofishing");
  }

  public void openView(String id)
  {
    Region root = null;
    switch (id)
    {
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
