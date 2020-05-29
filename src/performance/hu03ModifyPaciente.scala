package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class hu03ModifyPaciente extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.medicalservice.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.js""", """.*.ico""", """.*.css"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

	val headers_2 = Map(
		"A-IM" -> "x-bm,gzip",
		"Accept-Encoding" -> "gzip, deflate",
		"Proxy-Connection" -> "keep-alive",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_6 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.medicalservice.com",
		"Upgrade-Insecure-Requests" -> "1")

    val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"
    val uri3 = "http://cdn.content.prod.cms.msn.com/singletile/summary/alias/experiencebyname/today"
	
	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
			.pause(12)
	}
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(18)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}
	
	object ListPacientesPersonales {
		val listPacientesPersonales = exec(http("listPacientesPersonales")
			.get("/pacientes/findByMedico/1")
			.headers(headers_0))
		.pause(20)
	}
	
	object ShowPaciente {
		val showPaciente = exec(http("showPaciente")
			.get("/pacientes/1")
			.headers(headers_0))
		.pause(13)
	}
	
	object EditPacienteSuccess {
		val editPacienteSuccess = exec(http("editPacienteSuccess")
			.get("/pacientes/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("savePaciente")
			.post("/pacientes/1/edit")
			.headers(headers_6)
			.formParam("nombre", "Maria Gracia")
			.formParam("apellidos", "Castillo Castilla")
			.formParam("f_nacimiento", "1983/11/12")
			.formParam("DNI", "66100313S")
			.formParam("domicilio", "Camino Horno, 29")
			.formParam("n_telefono", "605708609")
			.formParam("email", "mariagracia_83@gmail.com")
			.formParam("f_alta", "2020/03/20")
			.formParam("medico", "1")
			.formParam("citas", "")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}
	
	object EditPacienteFailed {
		val editPacienteFailed = exec(http("editPacienteFailed")
			.get("/pacientes/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("savePaciente")
			.post("/pacientes/1/edit")
			.headers(headers_6)
			.formParam("nombre", "")
			.formParam("apellidos", "Castillo Castilla")
			.formParam("f_nacimiento", "1983/11/12")
			.formParam("DNI", "66100313S")
			.formParam("domicilio", "Camino Horno, 29")
			.formParam("n_telefono", "605708609")
			.formParam("email", "mariagracia_83@gmail.com")
			.formParam("f_alta", "2020/03/20")
			.formParam("medico", "1")
			.formParam("citas", "")
			.formParam("_csrf", "${stoken}"))
		.pause(22)
	}
	
	val editSuccess = scenario("EditarPaciente").exec(Home.home,
											Login.login,
											ListPacientesPersonales.listPacientesPersonales,
											ShowPaciente.showPaciente,
											EditPacienteSuccess.editPacienteSuccess
											)

	val editFailed = scenario("EditarPacienteFail").exec(Home.home,
											Login.login,
											ListPacientesPersonales.listPacientesPersonales,
											ShowPaciente.showPaciente,
											EditPacienteFailed.editPacienteFailed)

	setUp(
		editSuccess.inject(rampUsers(3000) during (100 seconds)),
		editFailed.inject(rampUsers(3000) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}