package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class InformeHistoriaClinica extends Simulation {

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
		.pause(4)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(3)
		.exec(http("LoggedIn")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
        	.formParam("_csrf", "${stoken}"))
		.pause(4)
	}

	object ListCitas {
		val listCitas = exec(http("ListCitas")
			.get("/citas")
			.headers(headers_0))
		.pause(9)
	}

	object ShowNotAddedInforme {
		val showNotAddedInforme = exec(http("ShowNotAddedInforme")
			.get("/citas/8/informes/2")
			.headers(headers_0))
		.pause(3)
	}

	object AddInformeToHistoriaClinica {
		val addInformeToHistoriaClinica = exec(http("AddInformeToHistoriaClinica")
			.get("/citas/8/informes/2/addtohistoriaclinica")
			.headers(headers_0))
		.pause(10)
	}

	object ShowAddedInforme {
		val showAddedInforme = exec(http("ShowAddedInforme")
			.get("/citas/1/informes/1")
			.headers(headers_0))
		.pause(2)
	}

	object DeleteInformeToHistoriaClinica {
		val deleteInformeToHistoriaClinica = exec(http("DeleteInformeToHistoriaClinica")
			.get("/citas/1/informes/1/detelefromhistoriaclinica")
			.headers(headers_0))
	}

	val scnAddInformeToHistoriaClinica = scenario("AddInformeHistoriaClinica").exec(Home.home,
																					Login.login,
																					ListCitas.listCitas,
																					ShowNotAddedInforme.showNotAddedInforme,
																					AddInformeToHistoriaClinica.addInformeToHistoriaClinica
	)


	val scnDeleteInformeFromHistoriaClinica = scenario("DeleteInformeHistoriaClinica").exec(Home.home,
																							Login.login,
																							ListCitas.listCitas,
																							ShowAddedInforme.showAddedInforme,
																							DeleteInformeToHistoriaClinica.deleteInformeToHistoriaClinica
	)

	setUp(scnAddInformeToHistoriaClinica.inject(rampUsers(7200) during (100 seconds)),
		  scnDeleteInformeFromHistoriaClinica.inject(rampUsers(7200) during (100 seconds)))
	.protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}