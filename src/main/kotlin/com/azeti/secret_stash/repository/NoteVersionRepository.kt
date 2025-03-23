package com.azeti.secret_stash.repository

import com.azeti.secret_stash.model.NoteVersion
import org.springframework.data.jpa.repository.JpaRepository

interface NoteVersionRepository : JpaRepository<NoteVersion, Long> {
}