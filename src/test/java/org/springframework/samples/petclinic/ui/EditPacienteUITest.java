package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

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
public class EditPacienteUITest {

	@LocalServerPort
	private int				port				= 8080;
	
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
	  public void testEditPacienteUITest() throws Exception {
		  this.driver.get("http://localhost:" + this.port);
		  this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		  this.driver.findElement(By.id("username")).clear();
		  this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
		  this.driver.findElement(By.id("password")).clear();
		  this.driver.findElement(By.id("password")).sendKeys("entrar");
		  this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		  driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		  driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[4]/a/span[2]")).click();
		  driver.findElement(By.xpath("//a[contains(@href, '/pacientes/3')]")).click();
		  driver.findElement(By.xpath("//a[contains(@href, '/pacientes/3/edit')]")).click();
		  driver.findElement(By.id("domicilio")).click();
		  driver.findElement(By.id("domicilio")).clear();
		  driver.findElement(By.id("domicilio")).sendKeys("Luis de Morales 27, Sevilla, Sevilla");
		  driver.findElement(By.xpath("//button[@type='submit']")).click();
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