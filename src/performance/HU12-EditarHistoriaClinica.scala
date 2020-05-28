package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditarHistoriaClinica extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jsp""", """.*.png""", """.*.ico""", """.*.css""", """.*.jar"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(10)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(22)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	object PacientesPersonales {
		val pacientesPersonales = exec(http("PacientesPersonales")
			.get("/pacientes/findByMedico/1"))
		.pause(16)
	}

	object VerPaciente {
		val verPaciente = exec(http("VerPaciente")
			.get("/pacientes/1"))
		.pause(11)
	}

	object VerHistoriaClinica {
		val verHistoriaClinica = exec(http("VerHistoriaClinica")
			.get("/pacientes/1/historiaclinica"))
		.pause(12)
	}

	object EditarHistoriaOk {
		val editarHistoriaOk = exec(http("EditarHistoriaClinica")
			.get("/pacientes/1/historiaclinica/edit")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(19)
		.exec(http("EditadoHistoria")
			.post("/pacientes/1/historiaclinica/edit")
			.headers(headers_2)
			.formParam("id", "1")
			.formParam("descripcion", "Descripcion modificado")
			.formParam("paciente", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object EditarHistoriaError {
		val editarHistoriaError = exec(http("EditarHistoriaClinica")
			.get("/pacientes/1/historiaclinica/edit")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(19)
		.exec(http("EditadoHistoriaError")
			.post("/pacientes/1/historiaclinica/edit")
			.headers(headers_2)
			.formParam("id", "1")
			.formParam("descripcion", "")
			.formParam("paciente", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}



	val success = scenario("EditarHistoriaClinicaOk").exec(Home.home,
												   Login.login,
												   PacientesPersonales.pacientesPersonales,
												   VerPaciente.verPaciente,
												   VerHistoriaClinica.verHistoriaClinica,
												   EditarHistoriaOk.editarHistoriaOk		
													)

	val error = scenario("EditarHistoriaClinicaError").exec(Home.home,
												   Login.login,
												   PacientesPersonales.pacientesPersonales,
												   VerPaciente.verPaciente,
												   VerHistoriaClinica.verHistoriaClinica,
												   EditarHistoriaError.editarHistoriaError		
													)

	

	setUp(success.inject(atOnceUsers(1)),error.inject(atOnceUsers(1))).protocols(httpProtocol)
}