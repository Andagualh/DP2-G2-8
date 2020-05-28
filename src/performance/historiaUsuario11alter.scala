package performance

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class historiaUsuario11alter extends Simulation {

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
			val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
			.pause(6)
		}

		object Login{
			val login = exec(http("login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("loginRe")
			.get("/login")
			.headers(headers_2)))
		.pause(7)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
		}

		object LisPacientePerso{
			val lisPacPer = exec(http("LisPacPer")
			.get("/pacientes/findByMedico/1")
			.headers(headers_0))
		.pause(10)
		}

		object Paciente1Datos{
			val pac1 = exec(http("pac_1")
			.get("/pacientes/9")
			.headers(headers_0))
		.pause(5)
		}

		object Pac1noHc {
			val pac1nohc = exec(http("pac1nohc")
			.get("/pacientes/9/historiaclinica")
			.headers(headers_0))
		.pause(6)
		}

		object Pac1CrearHc {
			val pac1crearhc = exec(http("crehc1")
			.get("/pacientes/9/historiaclinica/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(8)
		.exec(http("created_hc1")
			.post("/pacientes/9/historiaclinica/new")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("descripcion", "Probando Historia")
			.formParam("_csrf", "${stoken}"))
		.pause(33)
		}

		object LisPac {
			val lispac = exec(http("lispac")
			.get("/pacientes")
			.headers(headers_0))
		.pause(49)
		}

		object Pac2DiffMed {
			val pac2diff = exec(http("pac2_diffmed")
			.get("/pacientes/7")
			.headers(headers_0))
		.pause(15)
		}

		object Pac2NoHc {
			val pac2nohc = exec(http("pac2_diffmed_nohc")
			.get("/pacientes/7/historiaclinica")
			.headers(headers_0))
		.pause(13)
		}

		object Pac2cantcrear {
			val pac2cantcrear = exec(http("pac2_cant_crear_diffmed")
			.get("/pacientes/7/historiaclinica/new")
			.headers(headers_0))
		.pause(7)
		}



	val goodMedScn = scenario("MedicoPuedeCrear").exec(
		Home.home,
		Login.login,
		LisPacientePerso.lisPacPer,
		Paciente1Datos.pac1,
		Pac1noHc.pac1nohc,
		Pac1CrearHc.pac1crearhc,
	)

	val wrongMedScn = scenario("MedicoNoPuedeCrear").exec(
		Home.home,
		Login.login,
		LisPac.lispac,
		Pac2DiffMed.pac2diff,
		Pac2NoHc.pac2nohc,
		Pac2cantcrear.pac2cantcrear
	)

	setUp(
		goodMedScn.inject(rampUsers(3000) during (100 seconds)),
		wrongMedScn.inject(rampUsers(3000) during (100 seconds))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}