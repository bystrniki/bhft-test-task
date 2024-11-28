package org.bystritskiy.get

import io.qameta.allure.kotlin.Allure.step
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.bystritskiy.BaseTest
import org.bystritskiy.extensions.TodoCleanupExtension
import org.bystritskiy.utils.TodoUtils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@DisplayName("GET /todos")
class GetTodosTests : BaseTest() {

    private val createdTodoIds = mutableListOf<ULong>()

    @RegisterExtension
    val todoCleanupExtension = TodoCleanupExtension(createdTodoIds, client, adminAuthorizationHeader)

    @Test
    fun getTodosPositiveTest() {
        val generatedTodos = step("Generate todos") {
            buildList {
                repeat(10) {
                    add(TodoUtils.generateTodo())
                }
            }
        }

        step("Create the generated todos") {
            generatedTodos.forEach { todo ->
                client.createTodo(todo)
                createdTodoIds.add(todo.id)
            }
        }

        step("Verify all generated todos are returned") {
            val todos = client.getTodos()
            assertThat(todos)
                .`as`("Response contains all generated todos")
                .containsAll(generatedTodos)
        }
    }

    @Test
    fun getTodosWithLimitTest() {
        val generatedTodos = step("Generate todos") {
            buildList {
                repeat(10) {
                    add(TodoUtils.generateTodo())
                }
            }
        }

        step("Create the generated todos") {
            generatedTodos.forEach { todo ->
                client.createTodo(todo)
                createdTodoIds.add(todo.id)
            }
        }

        step("Retrieve todo list with limit and assert it") {
            val limit = 5
            val allTodos = client.getTodos(limit = limit)
            val todosWithLimit = client.getTodos(limit = limit)
            SoftAssertions().apply {
                assertThat(todosWithLimit.size)
                    .`as`("Retrieved todo list size is equal to $limit")
                    .isEqualTo(limit)
                assertThat(todosWithLimit)
                    .usingRecursiveComparison()
                    .isEqualTo(allTodos.take(limit))
            }.assertAll()
        }
    }

    @Test
    fun getTodosWithOffsetTest() {
        val generatedTodos = step("Generate todos") {
            buildList {
                repeat(10) {
                    add(TodoUtils.generateTodo())
                }
            }
        }

        step("Create the generated todos") {
            generatedTodos.forEach { todo ->
                client.createTodo(todo)
                createdTodoIds.add(todo.id)
            }
        }

        step("Retrieve todo list with offset and assert it") {
            val offset = 3
            val allTodos = client.getTodos()
            val todosWithOffset = client.getTodos(offset = offset)

            SoftAssertions().apply {
                assertThat(todosWithOffset.size)
                    .`as`("Retrieved todo list size is equal to total size minus offset")
                    .isEqualTo(allTodos.size - offset)

                assertThat(todosWithOffset)
                    .usingRecursiveComparison()
                    .isEqualTo(allTodos.drop(offset))
            }.assertAll()
        }
    }

    @Test
    fun getTodosWithOffsetAndLimitTest() {
        val generatedTodos = step("Generate todos") {
            buildList {
                repeat(10) {
                    add(TodoUtils.generateTodo())
                }
            }
        }

        step("Create the generated todos") {
            generatedTodos.forEach { todo ->
                client.createTodo(todo)
                createdTodoIds.add(todo.id)
            }
        }

        step("Retrieve todo list with offset and limit and assert it") {
            val offset = 3
            val limit = 5
            val allTodos = client.getTodos()
            val todosWithOffsetAndLimit = client.getTodos(offset = offset, limit = limit)

            SoftAssertions().apply {
                assertThat(todosWithOffsetAndLimit.size)
                    .`as`("Retrieved todo list size is equal to limit")
                    .isEqualTo(limit)

                assertThat(todosWithOffsetAndLimit)
                    .usingRecursiveComparison()
                    .isEqualTo(allTodos.drop(offset).take(limit))
            }.assertAll()
        }
    }
}