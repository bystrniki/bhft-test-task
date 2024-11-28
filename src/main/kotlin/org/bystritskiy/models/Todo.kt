package org.bystritskiy.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Todo(
    @field:JsonProperty("id")
    var id: ULong,
    @field:JsonProperty("text")
    var text: String,
    @field:JsonProperty("completed")
    var completed: Boolean
)