package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class hu18AddTratamientoToInformeALTER extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.medicalservice.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.js""", """.*.ico""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.medicalservice.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
		.pause(8)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}
	
	object ListCitasPersonales {
		val listCitasPersonales = exec(http("ListCitasPersonales")
			.get("/citas"))
		.pause(16)
	}
	
	object ShowInforme {
		val showInforme = exec(http("ShowInforme")
			.get("/citas/13/informes/5"))
		.pause(15)
	}
	
	object NewTratamiento {
		val newTratamiento = exec(http("NewTratamiento")
			.get("/tratamientos/new/5")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(15)
			.exec(http("TratamientoAdded")
			.post("/tratamientos/save")
			.headers(headers_2)
			.formParam("tratamiento id", "")
			.formParam("informe", "5")
			.formParam("medicamento", "Performance")
			.formParam("dosis", "Performance")
			.formParam("f_inicio_tratamiento", "2020-07-31")
			.formParam("f_fin_tratamiento", "2020-08-31")
			.formParam("id", "")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}
	
	object NewTratamientoFailed {
		val newTratamientoFailed = exec(http("NewTratamientoFailed")
			.get("/tratamientos/new/5")
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(20)
			.exec(http("TratamientoAddedFailed")
			.post("/tratamientos/save")
			.headers(headers_2)
			.formParam("tratamiento id", "")
			.formParam("informe", "5")
			.formParam("medicamento", "Performance")
			.formParam("dosis", "Performance")
			.formParam("f_inicio_tratamiento", "2020-07-31")
			.formParam("f_fin_tratamiento", "2020-07-15")
			.formParam("id", "")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}
	
	val newTratamiento = scenario("NewTratamiento").exec(Home.home,
											Login.login,
											ListCitasPersonales.listCitasPersonales,
											ShowInforme.showInforme,
											NewTratamiento.newTratamiento
											)

	val newTratamientoFailed = scenario("NewTratamientoFailed").exec(Home.home,
											Login.login,
											ListCitasPersonales.listCitasPersonales,
											ShowInforme.showInforme,
											NewTratamientoFailed.newTratamientoFailed
											)
											

	setUp(
		newTratamiento.inject(rampUsers(1300) during (100 seconds)),
		newTratamientoFailed.inject(rampUsers(1300) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}