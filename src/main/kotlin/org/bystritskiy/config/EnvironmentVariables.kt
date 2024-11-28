package org.bystritskiy.config

object EnvironmentVariables {
    val TODO_APP_TEST_BASE_URL: String = System.getenv("TODO_APP_TEST_BASE_URL") ?: "http://localhost:8080"
    val TODO_APP_LOAD_TEST_BASE_URL: String = System.getenv("TODO_APP_LOAD_TEST_BASE_URL") ?: "http://localhost:9090"
    val TEST_ADMIN_CREDENTIALS: String = System.getenv("TEST_ADMIN_CREDENTIALS") ?: "admin:admin"
}