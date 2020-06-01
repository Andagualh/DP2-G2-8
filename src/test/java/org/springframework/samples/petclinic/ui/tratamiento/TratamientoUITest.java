package org.springframework.samples.petclinic.ui.tratamiento;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TratamientoUITest {
	
	@LocalServerPort
	private int				port				= 8080;
	
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  
  @Autowired
  private CitaService citaService;

  @BeforeEach
  public void setUp() throws Exception {
	  
	  Cita cita = citaService.findCitaById(1).get();
	  cita.setFecha(LocalDate.now());
	  citaService.save(cita);
		
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Test
  public void testCreateTratamientoSuccess() throws Exception {
	this.driver.get("http://localhost:" + this.port);
	this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	this.driver.findElement(By.id("username")).clear();
	this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
	this.driver.findElement(By.id("password")).clear();
	this.driver.findElement(By.id("password")).sendKeys("entrar");
	this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.linkText("Ver Informe")).click();
    driver.findElement(By.linkText("Crear Tratamiento")).click();
    driver.findElement(By.id("medicamento")).click();
    driver.findElement(By.id("medicamento")).clear();
    driver.findElement(By.id("medicamento")).sendKeys("Espidifen");
    driver.findElement(By.id("dosis")).click();
    driver.findElement(By.id("dosis")).clear();
    driver.findElement(By.id("dosis")).sendKeys("1 sobre cada 8 horas");
    driver.findElement(By.id("f_inicio_tratamiento")).click();
    driver.findElement(By.linkText("29")).click();
    driver.findElement(By.id("f_fin_tratamiento")).click();
    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
    driver.findElement(By.linkText("15")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
  }
  
  @Test
  public void testCrearTratamientoTodosCamposVacios() throws Exception {
	this.driver.get("http://localhost:" + this.port);
	this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	this.driver.findElement(By.id("username")).clear();
	this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
	this.driver.findElement(By.id("password")).clear();
	this.driver.findElement(By.id("password")).sendKeys("entrar");
	this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.linkText("Ver Informe")).click();
    driver.findElement(By.linkText("Crear Tratamiento")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    Assert.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div/div")).getText());
    Assert.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[2]/div")).getText());
    Assert.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[3]/div")).getText());
    Assert.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[4]/div")).getText());
  }
  
  @Test
  public void testCreateTratamientoFechaFinMenorFechaInicio() throws Exception {
	this.driver.get("http://localhost:" + this.port);
	this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	this.driver.findElement(By.id("username")).clear();
	this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
	this.driver.findElement(By.id("password")).clear();
	this.driver.findElement(By.id("password")).sendKeys("entrar");
	this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.linkText("Ver Informe")).click();
    driver.findElement(By.linkText("Crear Tratamiento")).click();
    driver.findElement(By.id("medicamento")).click();
    driver.findElement(By.id("medicamento")).clear();
    driver.findElement(By.id("medicamento")).sendKeys("Espidifen");
    driver.findElement(By.id("dosis")).click();
    driver.findElement(By.id("dosis")).clear();
    driver.findElement(By.id("dosis")).sendKeys("1 sobre cada 8 horas");
    driver.findElement(By.id("f_inicio_tratamiento")).click();
    driver.findElement(By.linkText("5")).click();
    driver.findElement(By.id("f_fin_tratamiento")).click();
    driver.findElement(By.linkText("2")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    Assert.assertEquals("La fecha de finalización del tratamiento no puede ser anterior a la fecha de inicio del tratamiento", this.driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[4]/div")).getText());
  }
  
  @Test
  public void testEditTratamientoSuccess() throws Exception {
	this.driver.get("http://localhost:" + this.port);
	this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	this.driver.findElement(By.id("username")).clear();
	this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
	this.driver.findElement(By.id("password")).clear();
	this.driver.findElement(By.id("password")).sendKeys("entrar");
	this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.linkText("Ver Informe")).click();
    driver.findElement(By.linkText("Editar Tratamiento")).click();
    driver.findElement(By.id("medicamento")).click();
    driver.findElement(By.id("medicamento")).clear();
    driver.findElement(By.id("medicamento")).sendKeys("ibuprofeno");
    driver.findElement(By.id("dosis")).click();
    driver.findElement(By.id("dosis")).clear();
    driver.findElement(By.id("dosis")).sendKeys("1 pastilla cada 24 horas");
    driver.findElement(By.id("f_inicio_tratamiento")).click();
    driver.findElement(By.linkText("10")).click();
    driver.findElement(By.id("f_fin_tratamiento")).click();
    driver.findElement(By.linkText("31")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
  }
  
  @Test
  public void testEditTratamientoFechaFinMenorFechaInicio() throws Exception {
	this.driver.get("http://localhost:" + this.port);
	this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	this.driver.findElement(By.id("username")).clear();
	this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
	this.driver.findElement(By.id("password")).clear();
	this.driver.findElement(By.id("password")).sendKeys("entrar");
	this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.xpath("(//a[contains(text(),'Ver Informe')])[4]")).click();
    driver.findElement(By.linkText("Editar Tratamiento")).click();
    driver.findElement(By.id("f_fin_tratamiento")).click();
    driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
    driver.findElement(By.linkText("1")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    try {
      assertEquals("La fecha de finalización del tratamiento no puede ser anterior a la fecha de inicio del tratamiento", driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[4]/div/span[2]")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
  }
  
  @Test
  public void testEditarTratamientoTodosCamposVacios() throws Exception {
	this.driver.get("http://localhost:" + this.port);
	this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	this.driver.findElement(By.id("username")).clear();
	this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
	this.driver.findElement(By.id("password")).clear();
	this.driver.findElement(By.id("password")).sendKeys("entrar");
	this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a")).click();
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/ul/li[2]/a/span[2]")).click();
    driver.findElement(By.xpath("(//a[contains(text(),'Ver Informe')])[4]")).click();
    driver.findElement(By.linkText("Editar Tratamiento")).click();
    driver.findElement(By.id("medicamento")).click();
    driver.findElement(By.id("medicamento")).clear();
    driver.findElement(By.id("medicamento")).sendKeys("");
    driver.findElement(By.id("dosis")).click();
    driver.findElement(By.id("dosis")).clear();
    driver.findElement(By.id("dosis")).sendKeys("");
    driver.findElement(By.id("f_inicio_tratamiento")).click();
    driver.findElement(By.id("f_inicio_tratamiento")).clear();
    driver.findElement(By.id("f_inicio_tratamiento")).sendKeys("");
    driver.findElement(By.id("f_fin_tratamiento")).click();
    driver.findElement(By.id("f_fin_tratamiento")).clear();
    driver.findElement(By.id("f_fin_tratamiento")).sendKeys("");
    driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div[2]/div")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    try {
      assertEquals("no puede estar vacío", driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div/div/span[2]")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("no puede estar vacío", driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[2]/div/span[2]")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("no puede ser null", driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[3]/div/span[2]")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    try {
      assertEquals("no puede ser null", driver.findElement(By.xpath("//form[@id='add-tratamiento-form']/div/div[4]/div/span[2]")).getText());
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
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