package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class hu20DeleteTratamientoToInformeALTER extends Simulation {

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
		.pause(5)
	}

	object Login{
		val login =	exec(http("login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			)
		.pause(6)
		.exec(http("logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}


	object InformeEnter{
		val accessing = exec(http("listCitas")
			.get("/citas")
			.headers(headers_0))
		.pause(4)
		.exec(http("informeview")
			.get("/citas/13/informes/5")
			.headers(headers_0))
		.pause(23)
	}

	object BorrarTratamiento{
		val borrarTratamiento = exec(http("deltrat")
			.get("/tratamientos/delete/8")
			.headers(headers_0))
		.pause(9)
		
	}

	object InformeEnter2{
		val accessing2 = exec(http("cita_past")
			.get("/citas")
			.headers(headers_0))
		.pause(4)
		.exec(http("informe_past")
			.get("/citas/1/informes/1")
			.headers(headers_0))
		.pause(23)
	}

	object DeleteTratamientoCitaPasada{
		val deletepast = exec(http("delete_past")
			.get("/tratamientos/delete/1")
			.headers(headers_0))
		.pause(9)
	}

	val scn = scenario("intentarBorrarInex").exec(
		Home.home,
		Login.login,
		InformeEnter.accessing,
		BorrarTratamiento.borrarTratamiento

	)

	val pastScn = scenario("intentarBorrarPasado").exec(
		Home.home,
		Login.login,
		InformeEnter2.accessing2,
		DeleteTratamientoCitaPasada.deletepast

	)
	

	setUp(
		scn.inject(rampUsers(1900) during (100 seconds)),
		pastScn.inject(rampUsers(1900) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}