
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU02Diagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

	
	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
	.pause(9)
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
	
	object GetPacientesPersonales {
		val getPacientesPersonales = exec(http("GetPacientesPersonales")
			.get("/pacientes/findByMedico/3")
			.headers(headers_0))
		.pause(22)
	}
	
	object GetPacienteSinHC {
		val getPacienteSinHC = exec(http("GetPacienteSinHC")
			.get("/pacientes/7")
			.headers(headers_0))
		.pause(14)
	}
	
	object DeletePacienteSinHC {
		val deletePacienteSinHC = exec(http("DeletePacienteSinHC")
			.get("/pacientes/7/delete")
			.headers(headers_0))
		.pause(21)
	}
	
	object GetPacienteConHC {
		val getPacienteConHC = exec(http("GetPacienteConHC")
			.get("/pacientes/8")
			.headers(headers_0))
		.pause(12)
	}
	
	object DeletePacienteConHC {
		val deletePacienteConHC = exec(http("DeletePacienteConHC")
			.get("/pacientes/8/delete")
			.headers(headers_0))
		.pause(22)
	}
	
	

	val deleteSinHCScn = scenario ("DeleteSinHc").exec(Home.home, Login.login, GetPacientesPersonales.getPacientesPersonales, GetPacienteSinHC.getPacienteSinHC, DeletePacienteSinHC.deletePacienteSinHC)
	val deleteConHCScn = scenario ("DeleteConHc").exec(Home.home, Login.login, GetPacientesPersonales.getPacientesPersonales, GetPacienteConHC.getPacienteConHC, DeletePacienteConHC.deletePacienteConHC)
		


	setUp(
	deleteSinHCScn.inject(rampUsers(1500) during (15 seconds)),
	deleteConHCScn.inject(rampUsers(1500) during (15 seconds))
).protocols(httpProtocol)
.assertions(
	global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95)
	)
}