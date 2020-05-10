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
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CreateInformeHasMotErrorUITest {
    
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
  public void testCreateInformeHasMotErrorUITest() throws Exception {
    driver.get("http://localhost:"+this.port);
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("alvaroMedico");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("entrar");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//div/div/div/div")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[4]/a")).click();
    driver.findElement(By.xpath("//a[contains(@href, '/pacientes/1')]")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Crear\n			Cita')]")).click();
    driver.findElement(By.id("lugar")).click();
    driver.findElement(By.id("lugar")).clear();
    driver.findElement(By.id("lugar")).sendKeys("Lugar");
    driver.findElement(By.id("fecha")).click();
    driver.findElement(By.linkText(Integer.toString(LocalDate.now().getDayOfMonth()))).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString(), driver.findElement(By.xpath("//table[@id='citasTable']/tbody/tr[6]/td")).getText());
    assertEquals("Crear Informe", driver.findElement(By.xpath("//a[contains(@href, '10/informes/new')]")).getText());
    driver.findElement(By.xpath("//a[contains(@href, '10/informes/new')]")).click();
    driver.findElement(By.id("motivo_consulta")).click();
    driver.findElement(By.id("diagnostico")).click();
    driver.findElement(By.id("diagnostico")).clear();
    driver.findElement(By.id("diagnostico")).sendKeys("Diagn");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//form[@id='add-informe-form']/div/div/div")).click();
    assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='add-informe-form']/div/div/div/span[2]")).getText());
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