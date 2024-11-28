package org.bystritskiy.ws

import io.qameta.allure.kotlin.Allure.step
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.assertj.core.api.SoftAssertions
import org.bystritskiy.extensions.TodoCleanupExtension
import org.bystritskiy.utils.TodoUtils
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@DisplayName("WebSocket /ws")
class WebSocketTests : BaseWebSocketTest() {

    private var receivedMessage: String? = null
    private val messageWaitLatch = CountDownLatch(1)
    private var createdTodoIds = mutableListOf<ULong>()

    private inner class TestWebSocketClient : WebSocketClient(wsUri) {
        override fun onOpen(handshakeData: ServerHandshake?) {
            println("[WebSocketTests] Opened new WebSocket connection at $wsUri")
        }

        override fun onMessage(message: String?) {
            println("[WebSocketTests] Received message: $message");
            receivedMessage = message
            messageWaitLatch.countDown()
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            println("[WebSocketTests] WebSocket connection closed at $wsUri with exit code $code")
        }

        override fun onError(ex: Exception?) {
            println("[WebSocketTests] An error occurred: ${ex?.message}")
        }
    }

    @RegisterExtension
    val todoCleanupExtension = TodoCleanupExtension(createdTodoIds, client, adminAuthorizationHeader)

    @Test
    fun verifyWebSocketNotificationOnTodoCreationTest() {
        val wsClient = step("Connect to WebSocket") {
            TestWebSocketClient().apply { connectBlocking() }
        }

        val todo = step("Generate and create new todo") {
            TodoUtils.generateTodo().also { todo ->
                client.createTodo(todo)
                createdTodoIds.add(todo.id)
            }
        }

        step("Verify WebSocket notification received") {
            val isReceived = messageWaitLatch.await(10, TimeUnit.SECONDS)
            SoftAssertions().apply {
                assertThat(isReceived)
                    .`as`("WebSocket notification should be received within timeout")
                    .isTrue()
                assertThat(receivedMessage)
                    .`as`("WebSocket message should contain todo information")
                    .contains(todo.id.toString())
                    .contains(todo.text)
            }.assertAll()
        }

        step("Close WebSocket connection") {
            wsClient.closeBlocking()
        }
    }
}