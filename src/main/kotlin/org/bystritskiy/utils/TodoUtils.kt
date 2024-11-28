package org.bystritskiy.utils

import org.bystritskiy.api.impl.TodoClientImpl
import org.bystritskiy.models.Todo
import kotlin.random.Random
import kotlin.random.nextULong

object TodoUtils {

    fun generateTodo(
        id: ULong = Random.nextULong(100000UL),
        text: String = "todo",
        completed: Boolean = Random.nextBoolean()
    ) = Todo(
        id = id,
        text = text,
        completed = completed
    )

    fun getTodoById(client: TodoClientImpl, id: ULong): Todo? {
        return client.getTodos()
            .firstOrNull { it.id == id }
    }
}