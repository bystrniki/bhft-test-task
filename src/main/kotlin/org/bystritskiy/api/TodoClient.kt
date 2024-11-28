package org.bystritskiy.api

import org.bystritskiy.models.Todo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TodoClient {

    @GET("todos")
    fun getTodos(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): Call<List<Todo>>

    @POST("todos")
    fun createTodo(@Body todo: Todo): Call<Unit>

    @PUT("todos/{id}")
    fun updateTodo(
        @Path("id") id: ULong,
        @Body todo: Todo
    ): Call<Unit>

    @DELETE("todos/{id}")
    fun deleteTodo(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): Call<Unit>
}
