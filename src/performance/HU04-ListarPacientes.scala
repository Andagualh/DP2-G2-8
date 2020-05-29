package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListarPacientes extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.ico""", """.*.css""", """.*.js""", """.*.jpg"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(7)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(15)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object ListarPacientes {
		val listarPacientes = exec(http("ListarPacientes")
			.get("/pacientes"))
		.pause(13)
	}

	object BuscarPaciente {
		val buscarPaciente = exec(http("BuscarPaciente")
			.get("/pacientes/find"))
		.pause(13)
	}

	object BuscarPacienteSinDatos{
		val buscarPacienteSinDatos = exec(http("BuscarPacienteSinDatos")
			.get("/pacientes?apellidos="))
		.pause(29)
	}

	val casoNormal = scenario("BotonListarPaciente").exec(Home.home,
												   Login.login,
												   ListarPacientes.listarPacientes		
													)

	val usandoBuscar = scenario("BotonBuscarPacienteSinRellenar").exec(Home.home,
												   Login.login,
												   BuscarPaciente.buscarPaciente,
												   BuscarPacienteSinDatos.buscarPacienteSinDatos	
													)

	setUp(
		casoNormal.inject(rampUsers(500) during (20 seconds)),
		usandoBuscar.inject(rampUsers(500) during (20 seconds))
	)
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}