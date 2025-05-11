import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import model.ModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

public class MyApplication extends Application
{
  private Model model;

  @Override public void start(Stage primaryStage)
  {
    model = new ModelManager();
    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler view = new ViewHandler(viewModelFactory);
    view.start(primaryStage);
  }

  @Override public void stop() throws Exception
  {
    if (model != null)
    {
      if (model.getAutoClicker() != null)
      {
        model.stopAutoClicker();
      }
      if (model.getAutoFishing() != null)
      {
        model.stopFishing();
      }
      try
      {
        GlobalScreen.unregisterNativeHook();
      }
      catch (NativeHookException e)
      {
        System.err.println(
            "Error unregistering global hook: " + e.getMessage());
      }
    }
    super.stop();
  }
}