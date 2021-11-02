package ru.avbelyaev

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._


class SimulationCheckUsers extends Simulation {

  private val TARGET_RPS = 50
  private val TEST_TIME_MINUTES = 30
  private val BASE_CHECK_URL = "http://localhost:8080"

  // ^(?!id:.*).*$
  val random_request = csv("data/user-ids.csv").random

  private val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(BASE_CHECK_URL)
    .headers(Map(
      "x-trace-me" -> "duck"
    ))

  val scn = scenario("BasicSimulation")
    .feed(random_request)
    .exec(
      http("request_1")
        .post("/v1/users/${id}/do-smth")  // <- this "id" is one of the headers in CSV from above. values are taken from that column
        .header("Content-Type", "application/json")
        .body(StringBody(session =>
          s"""
             |{
             |  "foo": "bar"
             |}
             |""".stripMargin))
        .check(jsonPath("$.user_id").saveAs("USER_ID"))
    )

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
