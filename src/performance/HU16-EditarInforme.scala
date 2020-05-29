package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditarInforme extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.ico""", """.*.css""", """.*.js""", """.*.jpg"""), WhiteList())

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

    val uri2 = "http://www.google-analytics.com/__utm.gif"
    val uri3 = "http://ws.gtm.acer.com"

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(28)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}")
		).pause(14)
	}

	object CitasPersonales {
		val citasPersonales = exec(http("CitasPersonales")
			.get("/citas")
			.headers(headers_0))
		.pause(13)
	}

	object VerInforme {
		val verInforme = exec(http("VerInforme")
			.get("/citas/1/informes/1")
			.headers(headers_0))
		.pause(15)
	}

	object EditarInformeOk {
		val editarInformeOk = exec(http("EditarInforme")
			.get("/citas/1/informes/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(15)
		.exec(http("InformeEditado")
			.post("/citas/1/informes/1/edit")
			.headers(headers_2)
			.formParam("motivo_consulta", "motivo modificado")
			.formParam("diagnostico", "diagnostico")
			.formParam("cita", "1")
			.formParam("_csrf", "${stoken}")
		).pause(11)
	}

	object EditarInformeError {
		val editarInformeError = exec(http("EditarInforme")
			.get("/citas/1/informes/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(15)
		.exec(http("InformeEditError")
			.post("/citas/1/informes/1/edit")
			.headers(headers_2)
			.formParam("motivo_consulta", "")
			.formParam("diagnostico", "diagnostico")
			.formParam("cita", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	val success = scenario("EditarInformeOk").exec(Home.home,
												   Login.login,
												   CitasPersonales.citasPersonales,
												   VerInforme.verInforme,
												   EditarInformeOk.editarInformeOk		
													)

	val error = scenario("EditarInformeError").exec(Home.home,
												   Login.login,
												   CitasPersonales.citasPersonales,
												   VerInforme.verInforme,
												   EditarInformeError.editarInformeError		
													)

	

	setUp(
		success.inject(rampUsers(500) during (20 seconds)),
		error.inject(rampUsers(500) during (20 seconds))
	)
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}