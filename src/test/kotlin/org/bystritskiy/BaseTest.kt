package org.bystritskiy

import java.util.*
import org.bystritskiy.api.impl.TodoClientImpl
import org.bystritskiy.config.EnvironmentVariables

abstract class BaseTest {

    protected val client = TodoClientImpl(EnvironmentVariables.TODO_APP_TEST_BASE_URL)
    protected val adminAuthorizationHeader =
        "Basic ${
            Base64.getEncoder()
                .encodeToString(EnvironmentVariables.TEST_ADMIN_CREDENTIALS.toByteArray())
        }"
}