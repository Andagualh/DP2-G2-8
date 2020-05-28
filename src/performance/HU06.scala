
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU06Diagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.jpg""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
	}
	
	object Login{
			val login = exec(http("Login")
			.get("/login")
			.headers(headers_2)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(	
			http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "pabloMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
		}
	
	object GetPacientesByMedico {
		val getPacientesByMedico = exec(http("GetPacientesByMedico")
			.get("/pacientes/findByMedico/3")
			.headers(headers_0))
		.pause(43)
	}
	
	object Login2{
			val login2 = exec(http("Login2")
			.get("/login")
			.headers(headers_2)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(	
			http("Logged2")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "pedroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
		}
	
	object GetNoPacientesByMedico {
		val getNoPacientesByMedico = exec(http("GetNoPacientesByMedico")
			.get("/pacientes/findByMedico/4")
			.headers(headers_0))
		.pause(24)
	}

	val getPacientesPersonalesScn = scenario ("GetPacientesPersonales").exec(Home.home, Login.login, GetPacientesByMedico.getPacientesByMedico)
	val getNoPacientesPersonalesScn = scenario ("GetNoPacientesPersonales").exec(Home.home, Login2.login2, GetNoPacientesByMedico.getNoPacientesByMedico)


	setUp(
	getPacientesPersonalesScn.inject(rampUsers(2000) during (15 seconds)),
	getNoPacientesPersonalesScn.inject(rampUsers(2000) during (15 seconds))
).protocols(httpProtocol)	
.assertions(
	global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95)
	)
}