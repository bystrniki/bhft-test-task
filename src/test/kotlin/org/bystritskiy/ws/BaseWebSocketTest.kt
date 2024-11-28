package org.bystritskiy.ws

import java.net.URI
import org.bystritskiy.BaseTest
import org.bystritskiy.config.EnvironmentVariables

abstract class BaseWebSocketTest(baseUri: String = EnvironmentVariables.TODO_APP_TEST_BASE_URL) : BaseTest() {

    protected val wsUri = URI("ws://${baseUri.removePrefix("http://").removePrefix("https://")}/ws")
}