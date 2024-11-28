package org.bystritskiy.feeders

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.FeederBuilder
import kotlin.random.Random

object TodoFeeder {
    
    private val preGeneratedTodos = (1..1000).map { index ->
        mapOf(
            "id" to (10000UL + index.toULong()),
            "text" to "Todo Task $index",
            "completed" to Random.nextBoolean()
        )
    }.shuffled()

    fun todoFeeder(): FeederBuilder<Any> = CoreDsl.listFeeder(preGeneratedTodos)
}