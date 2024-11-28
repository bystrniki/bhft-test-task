package org.bystritskiy.api.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.bystritskiy.api.TodoClient
import org.bystritskiy.models.Todo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class TodoClientImpl(baseUrl: String) {

    private val api: TodoClient

    init {
        val mapper = ObjectMapper().registerKotlinModule()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()

        api = retrofit.create(TodoClient::class.java)
    }

    fun getTodos(offset: Int? = null, limit: Int? = null): List<Todo> {
        return api.getTodos(offset, limit).execute().body() ?: emptyList()
    }

    fun createTodo(todo: Todo): Response<Unit> {
        return api.createTodo(todo).execute()
    }

    fun updateTodo(id: ULong, todo: Todo): Response<Unit> {
        return api.updateTodo(id, todo).execute()
    }

    fun deleteTodo(id: ULong, authorization: String? = ""): Response<Unit> {
        return api.deleteTodo(id, authorization ?: "").execute()
    }
}