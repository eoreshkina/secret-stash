package com.azeti.secret_stash.repository

import com.azeti.secret_stash.model.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository : JpaRepository<Note, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): Note?
}