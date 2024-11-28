package org.bystritskiy.put

import io.qameta.allure.kotlin.Allure.step
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_OK
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.bystritskiy.BaseTest
import org.bystritskiy.extensions.TodoCleanupExtension
import org.bystritskiy.utils.TodoUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@DisplayName("PUT /todos/:id")
class PutTodosTests : BaseTest() {

    private var createdTodoIds = mutableListOf<ULong>()

    @RegisterExtension
    val todoCleanupExtension = TodoCleanupExtension(createdTodoIds, client, adminAuthorizationHeader)

    @Test
    fun updateExistingTodoTest() {
        val todo = step("Generate and create todo") {
            TodoUtils.generateTodo().also {
                client.createTodo(it)
                createdTodoIds.add(it.id)
            }
        }

        val updatedTodoData = step("Generate updated todo data") {
            todo.copy(
                text = "updated todo text",
                completed = !todo.completed
            )
        }

        step("Update the todo and verify response") {
            val response = client.updateTodo(todo.id, updatedTodoData)
            val retrievedUpdatedTodo = TodoUtils.getTodoById(client, todo.id)
            SoftAssertions().apply {
                assertThat(response.code())
                    .`as`("Updated todo should be returned")
                    .isEqualTo(HTTP_OK)
                assertThat(retrievedUpdatedTodo)
                    .usingRecursiveComparison()
                    .isEqualTo(updatedTodoData)
            }.assertAll()
        }
    }

    @Test
    fun updateNonExistentTodoTest() {
        val nonExistentId = 999999UL
        val updatedTodo = step("Generate todo data") {
            TodoUtils.generateTodo(nonExistentId)
        }

        step("Update non-existent todo and verify response returned error 404 Not Found ") {
            val response = client.updateTodo(nonExistentId, updatedTodo)
            assertThat(response.code())
                .`as`("Response returned error 404 Not Found")
                .isEqualTo(HTTP_NOT_FOUND)
        }
    }

    @Test
    fun updateTodoWithDifferentIdTest() {
        val todo = step("Generate and create initial todo") {
            TodoUtils.generateTodo().also {
                client.createTodo(it)
                createdTodoIds.add(it.id)
            }
        }

        val differentId = todo.id + 1UL

        step("Update todo with mismatched ID and verify response") {
            val response = client.updateTodo(differentId, todo)
            assertThat(response.code())
                .`as`("Response should return error 404 Not Found")
                .isEqualTo(HTTP_NOT_FOUND)
        }
    }
}