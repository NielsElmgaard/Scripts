// MyApplication.java
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

  @Override public void start(Stage primaryStage)
  {
    try
    {
      GlobalScreen.registerNativeHook();
    }
    catch (NativeHookException ex) {
      System.err.println("There was a problem registering the native hook.");
      System.err.println(ex.getMessage());
      System.exit(1);
    }
    Model model = new ModelManager();
    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler view = new ViewHandler(viewModelFactory);

    // Add your NativeKeyListeners here
    GlobalScreen.addNativeKeyListener(model.getAutoClicker());
    GlobalScreen.addNativeKeyListener(model.getAutoFishing());

    // Handle shutdown to unregister the hook
    primaryStage.setOnCloseRequest(event -> {
      try {
        GlobalScreen.unregisterNativeHook();
      } catch (NativeHookException e) {
        System.err.println("Failed to unregister native hook: " + e.getMessage());
      }
      // Add any other shutdown logic here if needed
    });

    view.start(primaryStage);
  }
}
