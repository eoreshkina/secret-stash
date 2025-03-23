package com.azeti.secret_stash

import com.azeti.secret_stash.repository.NoteRepository
import com.azeti.secret_stash.repository.NoteVersionRepository
import com.azeti.secret_stash.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class NoteService (
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
    private val noteVersionRepository: NoteVersionRepository
)