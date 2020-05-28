package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CrearCita extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.medicalservice.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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
		"Proxy-Connection" -> "keep-alive")


	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(9)	
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("request_5")
			.get("/login")
			.headers(headers_5)))
		.pause(20)
		.exec(http("Logged In")
			.post("/login")
			.headers(headers_6)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
        	.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	object ListPersonalPacientes {
		val listPersonalPacientes = exec(http("ListPersonalPacientes")
			.get("/pacientes/findByMedico/1")
			.headers(headers_0))
		.pause(23)
	}

	object ListDetailsPaciente {
		val listDetailsPaciente = exec(http("ListDetailsPaciente")
			.get("/pacientes/1")
			.headers(headers_0))
		.pause(11)
	}

	object NewCitaForm {
		val newCitaForm = exec(http("NewCitaForm")
			.get("/citas/new/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(51)
		.exec(http("ListPersonalCreatedCitas")
			.post("/citas/save")
			.headers(headers_6)
			.formParam("lugar", "Sevilla")
			.formParam("fecha", "2020-07-26")
			.formParam("paciente", "1")
			.formParam("id", "")
        	.formParam("_csrf", "${stoken}"))
		.pause(28)
	}

		object NewCitaFormError {
		val newCitaFormError = exec(http("NewCitaForm")
			.get("/citas/new/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(51)
		.exec(http("ListPersonalCreatedCitasError")
			.post("/citas/save")
			.headers(headers_7)
			.formParam("lugar", "Sevilla")
			.formParam("fecha", "2020-05-25")
			.formParam("paciente", "1")
			.formParam("id", "")
        	.formParam("_csrf", "${stoken}"))
		.pause(12)
	}

	val scnCrearCitaOk = scenario("CrearCita").exec(Home.home,
													Login.login,
													ListPersonalPacientes.listPersonalPacientes,
													ListDetailsPaciente.listDetailsPaciente,
													NewCitaForm.newCitaForm
													)

	val scnCrearCitaError = scenario("CrearCitaError").exec(Home.home,
													   		Login.login,
															ListPersonalPacientes.listPersonalPacientes,
															ListDetailsPaciente.listDetailsPaciente,
															NewCitaFormError.newCitaFormError
															)

	setUp(scnCrearCitaOk.inject(rampUsers(4900) during (100 seconds)),
		  scnCrearCitaError.inject(rampUsers(4900) during (100 seconds)))
	 .protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}