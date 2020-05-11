
package org.springframework.samples.petclinic.ui.paciente;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
public class CreatePacienteWithEmptyApellidosUITest {

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
	public void testCreatePacienteWithoutFormaContactoUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("entrar");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("ALVAROMEDICO", this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.linkText("22")).click();
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162J");
		driver.findElement(By.id("n_telefono")).click();
		driver.findElement(By.id("n_telefono")).clear();
		driver.findElement(By.id("n_telefono")).sendKeys("666123555");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("no puede estar vacío",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[2]/div/span[2]")).getText());
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
