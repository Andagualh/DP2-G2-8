
package org.springframework.samples.petclinic.ui;

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
	private int port = 8080;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("alvaroMedico");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("entrar");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("ALVAROMEDICO",
				this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	}

	@Test
	public void testCreatePacienteSuccessUITest() throws Exception {
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
		this.driver.findElement(By.id("domicilio"))
				.sendKeys("Calle Castillo Alcala de Guadaira, 19 5 D, Sevilla, Sevilla 41013");
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
		assertEquals("Calle Castillo Alcala de Guadaira, 19 5 D, Sevilla, Sevilla 41013",
				this.driver.findElement(By.xpath("//tr[4]/td")).getText());
		assertEquals("12345678Z", this.driver.findElement(By.xpath("//tr[5]/td")).getText());
		assertEquals("689810233", this.driver.findElement(By.xpath("//tr[6]/td")).getText());
		assertEquals("jesusroma@gmail.com", this.driver.findElement(By.xpath("//tr[7]/td")).getText());
		assertEquals("Alvaro Alferez", this.driver.findElement(By.linkText("Alvaro Alferez")).getText());
	}

	@Test
	public void testCreatePacienteWithEmptyApellidosUITest() throws Exception {
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

	@Test
	public void testCreatePacienteWithEmptyDNIUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.linkText("22")).click();
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("n_telefono")).click();
		driver.findElement(By.id("n_telefono")).clear();
		driver.findElement(By.id("n_telefono")).sendKeys("666123555");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	    try {
	        assertEquals("no puede estar vacío", driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[4]/div/span[2]")).getText());
	      } catch (Error e) {
	        verificationErrors.append(e.toString());
	      }
	}

	@Test
	public void testCreatePacienteWithEmptyFechaNacimientoUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162J");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("no puede ser null",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[3]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithEmptyNombreUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
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
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithInvalidDNIUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.linkText("22")).click();
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162");
		driver.findElement(By.id("n_telefono")).click();
		driver.findElement(By.id("n_telefono")).clear();
		driver.findElement(By.id("n_telefono")).sendKeys("666123555");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("la longitud tiene que estar entre 9 y 9",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[4]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithInvalidDNI2UITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.linkText("22")).click();
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162Z");
		driver.findElement(By.id("n_telefono")).click();
		driver.findElement(By.id("n_telefono")).clear();
		driver.findElement(By.id("n_telefono")).sendKeys("666123555");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("DNI invalido.",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[4]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithInvalidEmailUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162J");
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("123");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("no es una dirección de correo bien formada",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[7]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithInvalidFechaNacimientoUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("123");
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
			assertEquals(
					"Failed to convert property value of type java.lang.String to required type java.time.LocalDate for property f_nacimiento; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@javax.validation.constraints.PastOrPresent @org.springframework.format.annotation.DateTimeFormat @javax.validation.constraints.NotNull java.time.LocalDate] for value 123; nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [123]",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[3]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithInvalidMedicoUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162J");
		driver.findElement(By.id("n_telefono")).click();
		driver.findElement(By.id("n_telefono")).clear();
		driver.findElement(By.id("n_telefono")).sendKeys("123");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='2']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("No puedes crear un paciente para otro medico.",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[9]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithInvalidNumeroTelefonoUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a/span[2]")).click();
		driver.findElement(By.id("nombre")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Alvaro");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).click();
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/12/22");
		driver.findElement(By.id("DNI")).click();
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("18123162J");
		driver.findElement(By.id("n_telefono")).click();
		driver.findElement(By.id("n_telefono")).clear();
		driver.findElement(By.id("n_telefono")).sendKeys("123");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.linkText("9")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			assertEquals("Telefono debe tener 9 digitos",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[6]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}

	@Test
	public void testCreatePacienteWithoutFormaContactoUITest() throws Exception {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/ul/li[6]/a")).click();
		driver.findElement(By.id("nombre")).clear();
		driver.findElement(By.id("nombre")).sendKeys("Lorenzo");
		driver.findElement(By.id("apellidos")).clear();
		driver.findElement(By.id("apellidos")).sendKeys("Aliaga Rojano");
		driver.findElement(By.id("f_nacimiento")).clear();
		driver.findElement(By.id("f_nacimiento")).sendKeys("1997/02/28");
		driver.findElement(By.id("DNI")).clear();
		driver.findElement(By.id("DNI")).sendKeys("65574633T");
		driver.findElement(By.id("f_alta")).clear();
		driver.findElement(By.id("f_alta")).sendKeys("2020/04/20");
		driver.findElement(By.linkText("20")).click();
		driver.findElement(By.id("medico")).click();
		driver.findElement(By.xpath("//option[@value='1']")).click();
		driver.findElement(By.xpath("//form[@id='add-paciente-form']/div[2]/div/button")).click();
		assertEquals("No tiene forma de contacto.",
				driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[5]/div/span[2]")).getText());
	}

	@Test
	public void testCreatePacienteInvalidFechaAltaUITest() throws Exception {
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
		this.driver.findElement(By.id("domicilio"))
				.sendKeys("Calle Castillo Alcala de Guadaira, 19 5 D, Sevilla, Sevilla 41013");
		this.driver.findElement(By.id("n_telefono")).clear();
		this.driver.findElement(By.id("n_telefono")).sendKeys("689810233");
		this.driver.findElement(By.id("email")).clear();
		this.driver.findElement(By.id("email")).sendKeys("jesusroma@gmail.com");
		driver.findElement(By.id("f_alta")).click();
		driver.findElement(By.id("f_alta")).clear();
		driver.findElement(By.id("f_alta")).sendKeys("123");
		driver.findElement(By.xpath("//form[@id='add-paciente-form']/div")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.id("medico")).click();
		this.driver.findElement(By.xpath("//option[@value='1']")).click();
		this.driver.findElement(By.xpath("//form[@id='add-paciente-form']/div[2]/div/button")).click();
		try {
			assertEquals(
					"Failed to convert property value of type java.lang.String to required type java.time.LocalDate for property f_alta; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@org.springframework.format.annotation.DateTimeFormat @javax.validation.constraints.NotNull java.time.LocalDate] for value 123; nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [123]",
					driver.findElement(By.xpath("//form[@id='add-paciente-form']/div/div[8]/div/span[2]")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
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
