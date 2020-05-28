package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class historiaUsuario5 extends Simulation {

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

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.medicalservice.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home{
		val home = exec(http("home")
			.get("/")
			.headers(headers_0))
		.pause(6) 
	}

	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("LoginRe")
			.get("/login")
			.headers(headers_2))
			).pause(5)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object ListadoPacientes{
		val listadoPac = exec(http("listado_pacientes")
			.get("/pacientes")
			.headers(headers_0))
		.pause(8)
	}

	object DatosPaciente1{
		val datosPac1 = exec(http("paciente1")
			.get("/pacientes/1")
			.headers(headers_0))
		.pause(7)
	}

	object DatosPaciente2{
		val datosPac2 = exec(http("paciente2")
			.get("/pacientes/2")
			.headers(headers_0))
		.pause(12)
	}

	object DatosPaciente3{
		val datosPac3 = exec(http("paciente3")
			.get("/pacientes/3")
			.headers(headers_0))
		.pause(9)
	}

	object DatosPacienteNoExiste{
		val datosNonEx = exec(http("paciente_no_existe")
			.get("/pacientes/9999")
			.headers(headers_0)
			.resources(http("pacienteNonExist")
			.get("/resources/images/skull.jpg")
			.headers(headers_2)))
		.pause(10)
	}

	val medicoScn = scenario("DatosPaciente").exec(Home.home,
											Login.login,
											ListadoPacientes.listadoPac,
											DatosPaciente1.datosPac1
											)

	val nonExistScn = scenario("DatosPacienteNoExiste").exec(Home.home,
															Login.login,
															ListadoPacientes.listadoPac,
															DatosPaciente1.datosPac1,
															DatosPacienteNoExiste.datosNonEx)

	setUp(
		medicoScn.inject(rampUsers(3550) during (100 seconds)),
		nonExistScn.inject(rampUsers(3550) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
	
}