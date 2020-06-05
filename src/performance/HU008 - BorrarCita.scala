package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BorrarCita extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Accept" -> "*/*",
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-CryptoAPI/10.0")

	val uri2 = "http://ctldl.windowsupdate.com/msdownload/update/v3/static/trustedr/en/authrootstl.cab"
	
	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
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
		.pause(29)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(59)
		// Login
	}


	object ShowCitasPersonales {
		val showCitasPersonales = exec(http("ShowCitasPersonales")
			.get("/citas")
			.headers(headers_0))
		.pause(21)
		// ShowCitasPersonales
	}


	object DeleteCita {
		val deleteCita = exec(http("DeleteCita")
			.get("/citas/delete/3")
			.headers(headers_0))
		.pause(38)
		// DeleteCita
	}

	object DeleteCitaPast {
		val deleteCitaPast = exec(http("DeleteCitaPast")
			.get("/citas/delete/9")
			.headers(headers_0))
		//DeleteCitaPast
	}


	val okScn = scenario("BorrarCitaOk").exec(
		Home.home,
		Login.login,
		ShowCitasPersonales.showCitasPersonales,
		DeleteCita.deleteCita
	)

	val pastScn = scenario("BorrarCitaPast").exec(
		Home.home,
		Login.login,
		ShowCitasPersonales.showCitasPersonales,
		DeleteCitaPast.deleteCitaPast
	)

	setUp(
		okScn.inject(rampUsers(2500) during (100 seconds)),
		pastScn.inject(rampUsers(2500) during (100 seconds))
	).protocols(httpProtocol)
	 .assertions(
		 global.responseTime.max.lt(5000),
		 global.successfulRequests.percent.gt(95),
		 global.responseTime.mean.lt(1000)
	 )
}