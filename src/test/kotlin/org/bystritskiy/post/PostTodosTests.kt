package org.bystritskiy.post

import io.qameta.allure.kotlin.Allure.step
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.bystritskiy.BaseTest
import org.bystritskiy.extensions.TodoCleanupExtension
import org.bystritskiy.utils.TodoUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@DisplayName("POST /todos")
class PostTodosTests : BaseTest() {

    private var createdTodoIds = mutableListOf<ULong>()

    @RegisterExtension
    val todoCleanupExtension = TodoCleanupExtension(createdTodoIds, client, adminAuthorizationHeader)

    @Test
    fun createTodoTest() {
        val todo = step("Generate todo") {
            TodoUtils.generateTodo()
        }

        step("Create the generated todo") {
            client.createTodo(todo)
            createdTodoIds.add(todo.id)
        }

        step("Check whether created todo was added to the todos list") {
            TodoUtils.getTodoById(client, todo.id)
        }
    }

    @Test
    fun createSameTodoTwiceTest() {
        val todo = step("Generate todo") {
            TodoUtils.generateTodo()
        }

        step("Create the todo for the first time") {
            client.createTodo(todo)
            createdTodoIds.add(todo.id)
        }

        step("Attempt to create the same todo again") {
            val response = client.createTodo(todo)
            SoftAssertions().apply {
                assertThat(response.isSuccessful)
                    .`as`("Failed to create todo")
                    .isFalse()
                assertThat(response.code())
                    .`as`("Response code is equal to 400 Bad Request")
                    .isEqualTo(HTTP_BAD_REQUEST)
            }.assertAll()
        }

        step("Verify only one todo was created") {
            val todos = client.getTodos()
            assertThat(todos.count { it.id == todo.id })
                .`as`("Only one todo was created")
                .isEqualTo(1)
        }
    }
}