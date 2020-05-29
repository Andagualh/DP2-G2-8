package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class hu13ShowHC extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.medicalservice.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.js""", """.*.ico""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Origin" -> "http://www.medicalservice.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map("Accept" -> "image/webp,*/*")


	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
			.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(15)
		.exec(http("Loged")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	
	object ShowPacientes {
		val showPacientes = exec(http("ShowPacientes")
			.get("/pacientes/findByMedico/1")
			.headers(headers_0))
		.pause(12)
	}
	
	object ShowPacienteMaria {
		val showPacientesMaria = exec(http("ShowPacienteMaria")
			.get("/pacientes/1")
			.headers(headers_0))
		.pause(20)
	}
	
	object ShowHistoriaClinica {
		val showHistoriaClinica = exec(http("ShowHistoriaClinica")
			.get("/pacientes/1/historiaclinica")
			.headers(headers_0))
		.pause(22)
	}
	
	object ShowHCNoExiste {
		val showHCNoExiste = exec(http("ShowHCNoExiste")
			.get("/pacientes/9999/historiaclinica")
			.headers(headers_0)
			.resources(http("request_6")
			.get("/resources/images/skull.jpg")
			.headers(headers_6)))
		.pause(20)
	}

	val showHCExiste = scenario("Existe").exec(Home.home, 
												Login.login,
												ShowPacientes.showPacientes,
												ShowPacienteMaria.showPacientesMaria,
												ShowHistoriaClinica.showHistoriaClinica)
												
	val showHCNoExiste = scenario("NoExiste").exec(Home.home,
												Login.login,
												ShowPacientes.showPacientes,
												ShowPacienteMaria.showPacientesMaria,
												ShowHCNoExiste.showHCNoExiste)

	setUp(
		showHCExiste.inject(rampUsers(3800) during (100 seconds)),
		showHCNoExiste.inject(rampUsers(3800) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}