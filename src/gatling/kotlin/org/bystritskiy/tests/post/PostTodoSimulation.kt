package org.bystritskiy.tests.post

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.rampUsers
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import java.net.HttpURLConnection.HTTP_CREATED
import org.bystritskiy.config.EnvironmentVariables
import org.bystritskiy.feeders.TodoFeeder

class PostTodoSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl(EnvironmentVariables.TODO_APP_LOAD_TEST_BASE_URL)
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val scenario: ScenarioBuilder = scenario("Create Todo")
        .feed(TodoFeeder.todoFeeder())
        .exec(
            http("POST /todos")
                .post("/todos")
                .body(StringBody("""{"id": #{id}, "text": "#{text}", "completed": #{completed}}""")).asJson()
                .check(status().shouldBe(HTTP_CREATED))
        )

    init {
        setUp(
            scenario.injectOpen(
                atOnceUsers(1),
                rampUsers(10).during(5),
                constantUsersPerSec(5.0).during(30)
            )
        ).protocols(httpProtocol)
    }
}