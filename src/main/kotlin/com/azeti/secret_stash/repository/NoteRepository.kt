package com.azeti.secret_stash.repository

import com.azeti.secret_stash.model.Note
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<Note, Long> {
}