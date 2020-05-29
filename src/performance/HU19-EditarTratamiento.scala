package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditarTratamiento extends Simulation {

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
		.pause(20)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
			).pause(10)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	object ListarCitas {
		val listarCitas = exec(http("ListarCitas")
			.get("/citas"))
		.pause(9)
	}

	object VerInforme {
		val verInforme = exec(http("VerInforme")
			.get("/citas/1/informes/1"))
		.pause(12)
	}

	object EditarTratamientoOk {
		val editarTratamientoOk = exec(http("EditarTratamiento")
			.get("/tratamientos/1/edit")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(19)
		.exec(http("TratamientoEditadoOk")
			.post("/tratamientos/save")
			.headers(headers_2)
			.formParam("tratamiento id", "")
			.formParam("informe", "1")
			.formParam("medicamento", "paracetamol modificado")
			.formParam("dosis", "1 pastilla cada 4 horas")
			.formParam("f_inicio_tratamiento", "2020-03-09")
			.formParam("f_fin_tratamiento", "2020-12-24")
			.formParam("id", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}

	object EditarTratamientoError {
		val editarTratamientoError = exec(http("EditarTratamiento")
			.get("/tratamientos/1/edit")
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(19)
		.exec(http("TratamientoEditarError")
			.post("/tratamientos/save")
			.headers(headers_2)
			.formParam("tratamiento id", "")
			.formParam("informe", "1")
			.formParam("medicamento", "paracetamol modificado")
			.formParam("dosis", "1 pastilla cada 4 horas")
			.formParam("f_inicio_tratamiento", "2020-03-26")
			.formParam("f_fin_tratamiento", "2020-03-01")
			.formParam("id", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	val success = scenario("EditarTratamientoOk").exec(Home.home,
												   Login.login,
												   ListarCitas.listarCitas,
												   VerInforme.verInforme,
												   EditarTratamientoOk.editarTratamientoOk		
													)

	val error = scenario("EditarTratamientoError").exec(Home.home,
												   Login.login,
												   ListarCitas.listarCitas,
												   VerInforme.verInforme,
												   EditarTratamientoError.editarTratamientoError		
													)
		

	setUp(
		success.inject(rampUsers(1400) during (10 seconds)),
		error.inject(rampUsers(1400) during (10 seconds))
	)
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}