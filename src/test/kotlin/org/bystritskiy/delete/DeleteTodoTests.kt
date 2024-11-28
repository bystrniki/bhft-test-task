package org.bystritskiy.delete

import io.qameta.allure.kotlin.Allure.step
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import org.assertj.core.api.Assertions.assertThat
import org.bystritskiy.BaseTest
import org.bystritskiy.extensions.TodoCleanupExtension
import org.bystritskiy.utils.TodoUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("DELETE /todos/:id")
class DeleteTodoTests : BaseTest() {

    private var createdTodoIds = mutableListOf<ULong>()

    @RegisterExtension
    val todoCleanupExtension = TodoCleanupExtension(createdTodoIds, client, adminAuthorizationHeader)

    @ParameterizedTest
    @ValueSource(strings = ["0", "1844674407370", "18446744073709551615"])
    fun deleteTaskPositiveTest(id: String) {
        val todo = step("Generate todo") {
            TodoUtils.generateTodo(id.toULong())
        }

        step("Create the generated todo") {
            client.createTodo(todo)
            createdTodoIds.add(todo.id)
        }

        step("Delete created todo and verify whether it is successfully deleted") {
            val response = client.deleteTodo(todo.id, adminAuthorizationHeader)
            assertThat(response.code())
                .`as`("Created todo is successfully deleted")
                .isEqualTo(HTTP_NO_CONTENT)
        }
    }

    @Test
    fun deleteTodoWithoutAuthTest() {
        val todo = step("Generate todo") {
            TodoUtils.generateTodo()
        }

        step("Create the generated todo") {
            client.createTodo(todo)
            createdTodoIds.add(todo.id)
        }

        step("Delete created todo and verify response returned error 401 Unauthorized") {
            val response = client.deleteTodo(todo.id)
            assertThat(response.code())
                .`as`("Response returned error 401 Unauthorized")
                .isEqualTo(HTTP_UNAUTHORIZED)
        }
    }

    @Test
    fun deleteNonExistentTodoTest() {
        step("Delete todo with non-existent ID and verify response returned error 404 Not Found") {
            val response = client.deleteTodo(999999u, adminAuthorizationHeader)
            assertThat(response.code())
                .`as`("Response returned error 404 Not Found")
                .isEqualTo(HTTP_NOT_FOUND)
        }
    }
}