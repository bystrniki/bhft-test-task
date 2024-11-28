package org.bystritskiy.extensions

import org.bystritskiy.api.impl.TodoClientImpl
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TodoCleanupExtension(
    private val todoIds: MutableList<ULong>,
    private val client: TodoClientImpl,
    private val authHeader: String
) : AfterEachCallback {

    override fun afterEach(context: ExtensionContext) {
        todoIds.forEach { todoId ->
            client.deleteTodo(todoId, authHeader)
        }
        todoIds.clear()
    }
}