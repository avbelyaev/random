package ru.avbelyaev

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._


class SimulationCreateUsers extends Simulation {

  private val TARGET_RPS = 50
  private val TEST_TIME_MINUTES = 30
  private val BASE_URL = "http://localhost:8080"


  val rnd = new scala.util.Random

  private val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(BASE_URL)
    .headers(Map(
      "x-trace-me" -> "duck"
    ))

  val scn = scenario("BasicSimulation")
    .exec(
      http("request_1")
        .post("/v1/books")
        .header("Content-Type", "application/json")
        .body(StringBody(session =>
          s"""
             |{
             |  "first_name": "Anthony",
             |  "last_name": "Belyaev",
             |  "phone": "+${generatePhoneNumber()}"
             |}
             |""".stripMargin))
        .check(jsonPath("$.user_id").saveAs("USER_ID"))
    )
    .exec(session => {
      val id = session("USER_ID").as[String]
      println(s"id:$id")
      session
    })

  private def generatePhoneNumber(): String = {
    val start = 0
    val end = 999999
    val number = start + rnd.nextInt((end - start) + 1)
    "447840" + number
  }

  setUp(
    // create enormous number of users that will probably exceed the load we are sending
    scn.inject(
      constantUsersPerSec(100) during 30.minutes
    )
  )
    .protocols(httpProtocol)
    .throttle(
      // and throttle that huge amount of users with RPS setting here
      reachRps(TARGET_RPS) in 30.seconds,
      holdFor(TEST_TIME_MINUTES.minutes)
    )
}
