import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Main
{

  private static final String QUIZ_URL = "https://quiz.jyllands-posten.dk/flagkampen-2025";
  private static final int TOTAL_ROUNDS = 60;
  private static final String FLAG_SELECTOR = ".question__content-image img";
  private static final String ANSWER_SELECTOR = ".question__answers .question__answer-item";

  public static void main(String[] args)
  {
    WebDriverManager.chromedriver().setup();
    WebDriver driver = new ChromeDriver();

    try
    {
      runQuiz(driver);
    }
    catch (Exception e)
    {
      System.err.println("Error occured: " + e.getMessage());
    }
    finally
    {
      System.out.println("Existing browser...");
      driver.quit();
    }
  }

  private static void runQuiz(WebDriver driver) throws InterruptedException
  {
    driver.get(QUIZ_URL);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    String previousFlagUrl = "";

    for (int i = 1; i <= TOTAL_ROUNDS; i++)
    {
      System.out.println("--- Round " + i + " ---");

      WebElement flagElement = waitForNewFlag(driver, wait, previousFlagUrl);
      String currentFlagUrl = flagElement.getAttribute("src");
      previousFlagUrl = currentFlagUrl;

      String country = FlagData.getCountryByUrl(currentFlagUrl);

      if (country != null)
      {
        System.out.println("Searching for: " + country);
        handleAnswer(wait, country);
      }
      else
      {
        System.err.println("Unknown flag! Choosing first option.");
        clickFirstAvailableOption(wait);
      }

      // Delay before next question
      Thread.sleep(300);
    }

    System.out.println("The quiz is done!");
  }

  private static void handleAnswer(WebDriverWait wait, String country)
  {
    boolean clicked = findAndClickAnswer(wait, country);

    if (!clicked)
    {
      System.out.println(
          "Could not find the answer immediately. Trying again...");
      executionDelay(500);
      if (!findAndClickAnswer(wait, country))
      {
        clickFirstAvailableOption(wait);
      }
    }
  }

  private static boolean findAndClickAnswer(WebDriverWait wait, String country)
  {
    try
    {
      List<WebElement> options = wait.until(
          ExpectedConditions.presenceOfAllElementsLocatedBy(
              By.cssSelector(ANSWER_SELECTOR)));

      for (WebElement option : options)
      {
        if (option.getText().trim().equalsIgnoreCase(country))
        {
          option.click();
          return true;
        }
      }
    }
    catch (TimeoutException e)
    {
      return false;
    }
    return false;
  }

  private static void clickFirstAvailableOption(WebDriverWait wait)
  {
    try
    {
      List<WebElement> options = wait.until(
          ExpectedConditions.presenceOfAllElementsLocatedBy(
              By.cssSelector(ANSWER_SELECTOR)));
      if (!options.isEmpty())
      {
        options.get(0).click();
      }
    }
    catch (TimeoutException e)
    {
      System.err.println("Ingen svarmuligheder fundet.");
    }
  }

  private static WebElement waitForNewFlag(WebDriver driver, WebDriverWait wait,
      String oldUrl)
  {
    return wait.until(d -> {
      WebElement img = d.findElement(By.cssSelector(FLAG_SELECTOR));
      String src = img.getAttribute("src");
      // Only return the element if it is a NEW flag
      return !src.equals(oldUrl) ? img : null;
    });
  }

  private static void executionDelay(int ms)
  {
    try
    {
      Thread.sleep(ms);
    }
    catch (InterruptedException ignored)
    {
    }
  }
}