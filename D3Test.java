package edu.pitt.cs;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class D3Test {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  @Before
  public void setUp() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void tEST1LINKS() {
    driver.get("http://localhost:8080/");
    {
      WebElement element = driver.findElement(By.xpath("//a[contains(text(),\'Reset\')] "));
      String attribute = element.getAttribute("href");
      vars.put("refVal", attribute);
    }
    assertEquals(vars.get("refVal").toString(), "http://localhost:8080/reset"); // Prefix edit
  }

  @Test
  public void tEST3CATALOG() {
    driver.get("http://localhost:8080/");
    driver.findElement(By.linkText("Catalog")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("li:nth-child(3) > img"));
      String attribute = element.getAttribute("src");
      vars.put("imgSrc", attribute);
    }
    assertEquals(vars.get("imgSrc").toString(), "http://localhost:8080/images/cat2.jpg");
  }

  @Test
  public void tEST4LISTING() {
    driver.get("http://localhost:8080/");
    driver.findElement(By.linkText("Catalog")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@id=\'listing\']/ul/li[3]"));
      assert (elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[@id=\'listing\']/ul/li[4]"));
      assert (elements.size() == 0);
    }
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
  }

  @Test
  public void tEST5RENTACAT() {
    driver.get("http://localhost:8080/");
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[contains(.,\'Rent\')]"));
      assert (elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[contains(.,\'Return\')]"));
      assert (elements.size() > 0);
    }
  }

  @Test
  public void tEST6RENT() {
    driver.get("http://localhost:8080/");
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    driver.findElement(By.xpath("//input[@id=\'rentID\']")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id=\'rentID\']")));
    }
    driver.findElement(By.id("rentID")).sendKeys("1");
    driver.findElement(By.xpath("//button[contains(.,\'Rent\')]")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[1]")).getText(), is("Rented out"));
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
    assertThat(driver.findElement(By.id("rentResult")).getText(), is("Success!"));
  }

  @Test
  public void tEST2RESET() {
    driver.get("http://localhost:8080/");
    driver.findElement(By.linkText("Reset")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[1]")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div[@id=\'listing\']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
  }


  @Test
  public void tEST7RETURN() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(792, 840));
    js.executeScript("document.cookie = \"1=false\";document.cookie = \"2=true\";document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    driver.findElement(By.id("returnID")).click();
    driver.findElement(By.id("returnID")).sendKeys("2");
    driver.findElement(By.cssSelector(".form-group:nth-child(4) .btn")).click();
    assertThat(driver.findElement(By.xpath("//div/ul/li")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.xpath("//div/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.xpath("//div/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
    assertThat(driver.findElement(By.id("returnResult")).getText(), is("Success!"));
  }

  @Test
  public void tEST8FEEDACAT() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(794, 840));
    driver.findElement(By.linkText("Feed-A-Cat")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[contains(.,\'Feed\')]"));
      assert (elements.size() > 0);
    }
  }

  @Test
  public void tEST9FEED() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(798, 840));
    driver.findElement(By.linkText("Feed-A-Cat")).click();
    driver.findElement(By.id("catnips")).sendKeys("6");
    driver.findElement(By.xpath("//button[contains(.,\'Feed\')]")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
      wait.until(ExpectedConditions
          .visibilityOfElementLocated(By.xpath("//*[@id=\'feedResult\' and text()=\'Nom, nom, nom.\']")));
    }
    assertThat(driver.findElement(By.id("feedResult")).getText(), is("Nom, nom, nom."));
  }

  @Test
  public void tEST10GREETACAT() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(800, 840));
    driver.findElement(By.linkText("Greet-A-Cat")).click();
    assertThat(driver.findElement(By.xpath("//div[@id=\'greeting\']/h4")).getText(), is("Meow!Meow!Meow!"));
  }

  @Test
  public void tEST11GREETACATWITHNAME() {
    driver.get("http://localhost:8080/greet-a-cat/Jennyanydots");
    assertThat(driver.findElement(By.xpath("//div[@id=\'greeting\']/h4")).getText(), is("Meow! from Jennyanydots."));
  }
}