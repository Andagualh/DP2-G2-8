package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListCitas extends Simulation {

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

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.medicalservice.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

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
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(7)
		.exec(http("LoggedIn")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
        	.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object ListCitas {
		val listCitas = exec(http("ListCitas")
			.get("/citas")
			.headers(headers_0))
		.pause(11)
	}

	object ListCitasWrongMedico {
		val listCitasWrongMedico = exec(http("ListCitasWrongMedico")
			.get("/citas/2")
			.headers(headers_0))
	}

	val scnListCitas = scenario("ListCitas").exec(Home.home,
												  Login.login,
												  ListCitas.listCitas
	)

	val scnListCitasWrongMedico = scenario("ListCitasWrongMedico").exec(Home.home,
																		Login.login,
																		ListCitasWrongMedico.listCitasWrongMedico
	)

	setUp(scnListCitas.inject(rampUsers(8800) during (100 seconds)),
		  scnListCitasWrongMedico.inject(rampUsers(8800) during (100 seconds)))
	.protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}