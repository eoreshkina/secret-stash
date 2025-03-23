package com.azeti.secret_stash.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NoteRequest(
    val title: String,
    val content: String,
    val expirationTime: LocalDateTime? = null
)
