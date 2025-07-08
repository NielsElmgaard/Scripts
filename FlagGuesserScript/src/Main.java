import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class Main
{
  public static void main(String[] args)
  {
    WebDriverManager.chromedriver().setup();
    WebDriver driver = new ChromeDriver();

    try
    {
      driver.get("https://quiz.jyllands-posten.dk/flagkampen-2025");
      WebDriverWait webDriverWait = new WebDriverWait(driver,
          Duration.ofSeconds(5));

      String previousFlagUrl = "";

      for (int i = 0; i < 60; i++)
      {
        System.out.println("--- Round " + (i + 1) + " ---");

        // Quick check for flag change
        WebElement flagImage = waitForFlagChange(webDriverWait,
            previousFlagUrl);
        String flagImgSrc = flagImage.getAttribute("src");
        System.out.println("Flag: " + extractFlagId(flagImgSrc));

        previousFlagUrl = flagImgSrc;

        String correctCountry = FlagData.getCountryForFlagUrl(flagImgSrc);
        if (correctCountry == null)
        {
          System.err.println("Unknown flag - clicking first option");
          clickFirstOption(webDriverWait);
          continue;
        }

        System.out.println("Looking for: " + correctCountry);

        // Try to find and click the correct answer quickly
        if (findAndClickAnswer(webDriverWait, correctCountry))
        {
          System.out.println("✓ Clicked: " + correctCountry);
        }
        else
        {
          // Fallback: wait a bit longer and try again
          System.out.println("Retrying...");
          Thread.sleep(500);
          if (!findAndClickAnswer(webDriverWait, correctCountry))
          {
            System.err.println("Answer not found - clicking first option");
            clickFirstOption(webDriverWait);
          }
        }

        // Minimal wait for next question
        Thread.sleep(200);
      }

      System.out.println("Quiz completed!");
      Thread.sleep(Duration.ofMinutes(2).toMillis());

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      System.out.println("Script finished.");
    }
  }

  private static WebElement waitForFlagChange(WebDriverWait wait,
      String previousFlagUrl)
  {
    if (previousFlagUrl.isEmpty())
    {
      // First round - just wait for any flag
      return wait.until(ExpectedConditions.presenceOfElementLocated(
          By.cssSelector(".question__content-image img")));
    }

    // Wait for flag to change
    return wait.until(driver -> {
      try
      {
        WebElement flagImage = driver.findElement(
            By.cssSelector(".question__content-image img"));
        String currentFlagUrl = flagImage.getAttribute("src");
        return !currentFlagUrl.equals(previousFlagUrl) ? flagImage : null;
      }
      catch (Exception e)
      {
        return null;
      }
    });
  }

  private static boolean findAndClickAnswer(WebDriverWait wait,
      String correctCountry)
  {
    try
    {
      // Get all answer options
      List<WebElement> answerOptions = wait.until(
          ExpectedConditions.presenceOfAllElementsLocatedBy(
              By.cssSelector(".question__answers .question__answer-item")));

      // Look for the correct answer
      for (WebElement option : answerOptions)
      {
        String answerText = option.getText().trim();
        if (answerText.equalsIgnoreCase(correctCountry))
        {
          option.click();
          return true;
        }
      }
      return false;
    }
    catch (TimeoutException e)
    {
      return false;
    }
  }

  private static void clickFirstOption(WebDriverWait wait)
  {
    try
    {
      List<WebElement> options = wait.until(
          ExpectedConditions.presenceOfAllElementsLocatedBy(
              By.cssSelector(".question__answers .question__answer-item")));
      if (!options.isEmpty())
      {
        options.get(0).click();
      }
    }
    catch (TimeoutException e)
    {
      System.err.println("Could not find any answer options");
    }
  }

  private static String extractFlagId(String flagUrl)
  {
    int lastSlash = flagUrl.lastIndexOf('/');
    if (lastSlash != -1)
    {
      return flagUrl.substring(lastSlash + 1,
          lastSlash + 9); // First 8 chars of filename
    }
    return flagUrl;
  }
}