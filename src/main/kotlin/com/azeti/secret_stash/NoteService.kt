package com.azeti.secret_stash

import com.azeti.secret_stash.dto.NoteRequest
import com.azeti.secret_stash.model.Note
import com.azeti.secret_stash.repository.NoteRepository
import com.azeti.secret_stash.repository.NoteVersionRepository
import com.azeti.secret_stash.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class NoteService (
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
    private val noteVersionRepository: NoteVersionRepository
) {

    @Transactional
    fun createNote(userId: Long, request: NoteRequest): Note {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found") }

        val note = Note(
            title = request.title,
            content = request.content,
            user = user,
            expirationTime = request.expirationTime
        )

        return noteRepository.save(note)
    }

    fun getNoteById(noteId: Long, userId: Long): Note {
        return noteRepository.findByIdAndUserId(noteId, userId)
            ?: throw EntityNotFoundException("Note not found or you don't have access")
    }
}