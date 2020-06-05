package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class InformePrivado extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Purpose" -> "prefetch",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
		// Home
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(10).exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
		// Login
	}

	object ShowCitasPersonales {
		val showCitasPersonales = exec(http("ShowCitasPersonales")
			.get("/citas")
			.headers(headers_0))
		.pause(20)
		// ShowCitasPersonales
	}

	object ShowInforme {
		val showInforme = exec(http("ShowInforme")
			.get("/citas/9/informes/3")
			.headers(headers_0))
		.pause(14)
		// ShowInforme
	}

	object ShowInformeNotAuthorized {
		val showInformeNotAuthorized = exec(http("ShowInformeNotAuthorized")
			.get("/citas/9/informes/3")
			.headers(headers_6))
		.pause(1)
		.exec(http("request_13")
			.get("/citas/9/informes/3")
			.headers(headers_0))
		.pause(13)
		// ShowInformeNotAuthorized
	}

	val showOk = scenario("InformePrivadoOk").exec(
		Home.home,
		Login.login,
		ShowCitasPersonales.showCitasPersonales,
		ShowInforme.showInforme
	)

	val showWrong = scenario("InformePrivadoWrong").exec(
		Home.home,
		Login.login,
		ShowInformeNotAuthorized.showInformeNotAuthorized
	)
		

	setUp(
		showOk.inject(rampUsers(2500) during (100 seconds)),
		showWrong.inject(rampUsers(2500) during (100 seconds))
	).protocols(httpProtocol)
	 .assertions(
		 global.responseTime.max.lt(5000),
		 global.successfulRequests.percent.gt(95),
		 global.responseTime.mean.lt(1000)
	)
}