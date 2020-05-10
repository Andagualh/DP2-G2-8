
package org.springframework.samples.petclinic.ui.paciente;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreatePacienteUITest {

	@LocalServerPort
	private int				port				= 8080;

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testCreatePacienteUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("entrar");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a")).click();
		this.driver.findElement(By.id("nombre")).clear();
		this.driver.findElement(By.id("nombre")).sendKeys("Jesus");
		this.driver.findElement(By.id("apellidos")).clear();
		this.driver.findElement(By.id("apellidos")).sendKeys("Romero Martin");
		this.driver.findElement(By.id("f_nacimiento")).clear();
		this.driver.findElement(By.id("f_nacimiento")).sendKeys("2003/04/30");
		this.driver.findElement(By.id("DNI")).clear();
		this.driver.findElement(By.id("DNI")).sendKeys("12345678Z");
		this.driver.findElement(By.id("domicilio")).clear();
		this.driver.findElement(By.id("domicilio")).sendKeys("Calle Castillo Alcala de Guadaira, 19 5 D, Sevilla, Sevilla 41013");
		this.driver.findElement(By.id("n_telefono")).clear();
		this.driver.findElement(By.id("n_telefono")).sendKeys("689810233");
		this.driver.findElement(By.id("email")).clear();
		this.driver.findElement(By.id("email")).sendKeys("jesusroma@gmail.com");
		this.driver.findElement(By.id("medico")).click();
		this.driver.findElement(By.xpath("//option[@value='1']")).click();
		this.driver.findElement(By.xpath("//form[@id='add-paciente-form']/div[2]/div/button")).click();
		assertEquals("Jesus Romero Martin", this.driver.findElement(By.xpath("//td")).getText());
		assertEquals("2003-04-30", this.driver.findElement(By.xpath("//tr[2]/td")).getText());
		assertEquals(LocalDate.now().toString(), this.driver.findElement(By.xpath("//tr[3]/td")).getText());
		assertEquals("Calle Castillo Alcala de Guadaira, 19 5 D, Sevilla, Sevilla 41013", this.driver.findElement(By.xpath("//tr[4]/td")).getText());
		assertEquals("12345678Z", this.driver.findElement(By.xpath("//tr[5]/td")).getText());
		assertEquals("689810233", this.driver.findElement(By.xpath("//tr[6]/td")).getText());
		assertEquals("jesusroma@gmail.com", this.driver.findElement(By.xpath("//tr[7]/td")).getText());
		assertEquals("Alvaro Alferez", this.driver.findElement(By.linkText("Alvaro Alferez")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}
