import java.awt.*;

public class MouseCoordinateFinder {
  public static void main(String[] args) {
    // Create an event listener to get mouse position
    while (true) {
      // Get the current mouse position
      Point mousePos = MouseInfo.getPointerInfo().getLocation();
      System.out.println("Current Mouse Position: X = " + mousePos.x + ", Y = " + mousePos.y);

      try {
        Thread.sleep(500); // Sleep for half a second before checking again
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
