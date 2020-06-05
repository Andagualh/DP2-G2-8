package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteInforme extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Content-Type" -> "application/json",
		"Proxy-Connection" -> "keep-alive",
		"X-Goog-Update-AppId" -> "ihnlcenocehgdaegdmhbidjhnhdchfmm,gcmjkmgdlgnkkcocmoeiminaijmmjnii,oimompecagnajdejgnnjijobebaeigek,llkgjffcdpffmhiakmfcdcblohccpfmo,hfnkpimlhhgieaddgfemjhofmfblmnib,hnimpnehoodheedghdeeijklkeaacbdc,khaoiebndkojlmppeemjhbpbandiljpe,gkmgaooipdjhmangpemjhigmamcehddo,giekcmmlnklenlaomppkphknjmnnpneh,mimojjlkmoijpicakmndhoigimigcmbb,ehgidpndbllacpjalkiimkbadgjfnnmc,copjbmjbojbakpaedmpkhmiplmmehfck,jflookgnkcckhobaglndicnbbgbonegd,aemomkdncapdnfajjbbcbdebjljbpmpj,ggkkehgbnfjpeggfpleeakpidbkibbmn,bklopemakmnopmghhmccadeonafabnal",
		"X-Goog-Update-Interactivity" -> "bg",
		"X-Goog-Update-Updater" -> "chrome-83.0.4103.61")

    val uri2 = "http://update.googleapis.com/service/update2/json"


	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(5)
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
		.pause(6)
		.exec(http("request_3")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "alvaroMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
		// Login
	}

	object CitasPersonales {
		val citasPersonales = exec(http("CitasPersonales")
			.get("/citas")
			.headers(headers_0))
		.pause(21)
		// CitasPersonales
	}

	object ShowInforme {
		val showInforme = exec(http("ShowInforme")
			.get("/citas/13/informes/5")
			.headers(headers_0))
		.pause(6)
		.exec(http("request_6")
			.post(uri2 + "?cup2key=10:2312977199&cup2hreq=d710cfd57276a9b9263dc0748794e2d7455cf79ef41b22d181c2fd683080ec18")
			.headers(headers_6)
			.body(RawFileBody("dp2/deleteinforme/0006_request.json")))
		.pause(30)
		// ShowInforme
	}

	object BorrarInforme {
		val borrarInforme = exec(http("BorrarInforme")
			.get("/citas/13/informes/delete/5")
			.headers(headers_0))
		.pause(12)
		// BorrarInforme
	}

	object Login2 {
		val login2 = exec(http("Login2")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			.resources(http("request_11")
			.get("/login")
			.headers(headers_2)))
		.pause(6)
		.exec(http("request_12")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "pabloMedico")
			.formParam("password", "entrar")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
		// Login2
	}

	object WrongDelete {
		val wrongDelete = exec(http("WrongDelete")
			.get("/citas/13/informes/delete/5")
			.headers(headers_0))
		.pause(4)
		// WrongDelete
	}

	
	val deleteOk = scenario("BorrarInformeOk").exec(
		Home.home,
		Login.login,
		CitasPersonales.citasPersonales,
		ShowInforme.showInforme,
		BorrarInforme.borrarInforme
	)

	val deleteWrong = scenario("BorrarInformeWrong").exec(
		Home.home,
		Login2.login2,
		WrongDelete.wrongDelete
	)

	setUp(
		deleteOk.inject(rampUsers(2500) during (100 seconds)),
		deleteWrong.inject(rampUsers(2500) during (100 seconds))
	).protocols(httpProtocol)
	 .assertions(
		 global.responseTime.max.lt(5000),
		 global.successfulRequests.percent.gt(95),
		 global.responseTime.mean.lt(1000)
	 )
}