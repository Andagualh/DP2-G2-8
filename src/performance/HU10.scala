
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU10Diagnosis extends Simulation {

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
	
		object Home{
			val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
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
		
		object FormCitasByFecha{
			val formCitasByFecha = exec(http("FormCitasByFecha")
			.get("/citas/find")
			.headers(headers_0))
		.pause(32)
		}
		
		object GetCitasByFecha{
			val getCitasByFecha = exec(http("GetCitasByFecha")
			.get("/citas/porfecha?fecha=2015-04-20&paciente=")
			.headers(headers_0))
		.pause(17)
		}
		
		object GetNoCitasByFecha{
			val getNoCitasByFecha = exec(http("GetNoCitasByFecha")
			.get("/citas/porfecha?fecha=2020-05-27&paciente=")
			.headers(headers_0))
		.pause(11)
		}


		
		val getCitasPorFechaScn = scenario ("GetCitasPorFecha").exec(Home.home, Login.login, FormCitasByFecha.formCitasByFecha, GetCitasByFecha.getCitasByFecha)
		val getNoCitasPorFechaScn = scenario ("GetNoCitasPorFecha").exec(Home.home, Login.login, FormCitasByFecha.formCitasByFecha, GetNoCitasByFecha.getNoCitasByFecha)

	setUp(
	getCitasPorFechaScn.inject(rampUsers(2000) during (15 seconds)),
	getNoCitasPorFechaScn.inject(rampUsers(2000) during (15 seconds))
).protocols(httpProtocol)	
.assertions(
	global.responseTime.max.lt(5000),
	global.responseTime.mean.lt(1000),
	global.successfulRequests.percent.gt(95)
	)
}