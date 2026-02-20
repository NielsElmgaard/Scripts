import java.awt.MouseInfo;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

public class MouseCoordinateFinder
{

  public static void main(String[] args)
  {
    try
    {
      while (!Thread.currentThread().isInterrupted())
      {
        printMousePosition();
        TimeUnit.MILLISECONDS.sleep(500);
      }
    }
    catch (InterruptedException e)
    {
      System.err.println(
          MouseCoordinateFinder.class.getName() + " was interrupted.");
      Thread.currentThread().interrupt();
    }
  }

  private static void printMousePosition()
  {
    Point location = MouseInfo.getPointerInfo().getLocation();
    System.out.printf("X: %d | Y: %d%n", location.x, location.y);
  }
}