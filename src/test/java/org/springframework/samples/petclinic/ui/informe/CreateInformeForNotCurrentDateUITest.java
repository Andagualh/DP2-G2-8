package org.springframework.samples.petclinic.ui.informe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext

public class CreateInformeForNotCurrentDateUITest {

    @LocalServerPort
    private int port = 8080;

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeEach
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    
      @Test
    public void testCreateInformeForNotCurrentDate() throws Exception {
    driver.get("http://localhost:" + this.port);
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("alvaroMedico");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("entrar");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//div/div/div/div")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.xpath("//table[@id='citasTable']/tbody/tr[2]/td")).click();
    assertEquals("2025-07-09", driver.findElement(By.xpath("//table[@id='citasTable']/tbody/tr[2]/td")).getText());
    driver.findElement(By.xpath("//table[@id='citasTable']/tbody/tr[2]/td[4]")).click();
    assertEquals("No tiene informe", driver.findElement(By.xpath("//table[@id='citasTable']/tbody/tr[2]/td[4]")).getText());
     }
    
    
    @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }

}