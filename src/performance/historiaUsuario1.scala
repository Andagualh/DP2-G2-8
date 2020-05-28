package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class historiaUsuario1 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.medicalservice.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.png""", """.*.js""", """.*.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,es-ES;q=0.8,es;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "*/*",
		"Origin" -> "http://www.medicalservice.com",
		"Proxy-Connection" -> "keep-alive")

	val headers_5 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_6 = Map(
		"Origin" -> "http://www.medicalservice.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map(
		"Origin" -> "http://www.medicalservice.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")


		object Home {
			val home = exec(http("home")
			.get("/")
			.headers(headers_0))
		.pause(10)
		
		}

		object Login {
			val login = exec(http("login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("LoginRe")
			.get("/login")
			.headers(headers_5))
		).pause(15)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_6)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
		}

		object PacienteCrear{
			val createPaciente	= exec(http("create_paciente")
			.get("/pacientes/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(67)

			.exec(http("paciente_created")
			.post("/pacientes/new")
			.headers(headers_6)
			.formParam("nombre", "Paciente")
			.formParam("apellidos", "De Prueba")
			.formParam("f_nacimiento", "1997/12/12")
			.formParam("DNI", "02492545N")
			.formParam("domicilio", "Av. Reina Mercedes S/N")
			.formParam("n_telefono", "654343212")
			.formParam("email", "emaildeprueba@medical.com")
			.formParam("f_alta", "2020/05/26")
			.formParam("medico", "1")
			.formParam("citas", "")
			.formParam("_csrf", "${stoken}"))
			.pause(9)
		}
		object FailedCreation{
			val failCreation = 
			exec(http("create_paciente")
			.get("/pacientes/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(67)
			
			.exec(http("failed_creation")
			.post("/pacientes/new")
			.headers(headers_7)
			.formParam("nombre", "Paciente de Pruebas")
			.formParam("apellidos", "De Pruebas")
			.formParam("f_nacimiento", "1998/12/11")
			.formParam("DNI", "02492545N")
			.formParam("domicilio", "")
			.formParam("n_telefono", "")
			.formParam("email", "")
			.formParam("f_alta", "2020/05/26")
			.formParam("medico", "1")
			.formParam("citas", "")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
		// failedCreation
		}

	val medicoScn = scenario("CreacionPaciente").exec(Home.home,
											Login.login,
											PacienteCrear.createPaciente)

	val medicoFailedScn = scenario("CreacionPacienteSinContacto").exec(Home.home,
														Login.login,
														FailedCreation.failCreation)

		setUp(
		medicoScn.inject(rampUsers(4300) during (100 seconds)),
		medicoFailedScn.inject(rampUsers(4300) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}