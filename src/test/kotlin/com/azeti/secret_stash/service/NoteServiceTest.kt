package com.azeti.secret_stash.service

import com.azeti.secret_stash.NoteService
import com.azeti.secret_stash.dto.NoteRequest
import com.azeti.secret_stash.model.Note
import com.azeti.secret_stash.model.User
import com.azeti.secret_stash.repository.NoteRepository
import com.azeti.secret_stash.repository.NoteVersionRepository
import com.azeti.secret_stash.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.ArgumentMatchers.any
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class NoteServiceTest {
    @Mock
    private lateinit var noteRepository: NoteRepository

    @Mock
    private lateinit var noteVersionRepository: NoteVersionRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var noteService: NoteService

    @Captor
    private lateinit var notesCaptor: ArgumentCaptor<Note>

    private lateinit var testUser: User
    private lateinit var testNote: Note
    private lateinit var noteRequest: NoteRequest

    @BeforeEach
    fun setUp() {
        // Setup test user
        testUser = User(
            id = 1L,
            email = "test@example.com",
            password = "encoded_password",
            lastLogin = LocalDateTime.now()
        )

        // Setup test note
        testNote = Note(
            id = 1L,
            title = "Test Note",
            content = "This is a test note content",
            user = testUser,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            version = 1
        )

        // Setup note request
        noteRequest = NoteRequest(
            title = "Test Note",
            content = "This is a test note content",
            expirationTime = null
        )
    }

    @Test
    fun `should create new note`() {
        //given
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(testUser))
        `when`(noteRepository.save(any(Note::class.java))).thenReturn(testNote)

        //when
        val result = noteService.createNote(testUser.id, noteRequest)

        //then
        verify(noteRepository).save(notesCaptor.capture())
        val capturedNote = notesCaptor.value
        assertEquals(noteRequest.title, capturedNote.title)
        assertEquals(noteRequest.content, capturedNote.content)
        assertEquals(testUser, capturedNote.user)
        assertEquals(1, capturedNote.version)

        Assertions.assertNotNull(result)
        assertEquals(testNote.id, result.id)
    }

    @Test
    fun `createNote with expiration time should save note with correct expiration time`() {
        //given
        val expirationTime = LocalDateTime.now().plusDays(1)
        val noteRequestWithExpiration = NoteRequest(
            title = "Test Note with Expiration",
            content = "This note will self-destruct",
            expirationTime = expirationTime
        )

        val noteWithExpiration = Note(
            id = 2L,
            title = "Test Note with Expiration",
            content = "This note will self-destruct",
            user = testUser,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            expirationTime = expirationTime,
            version = 1
        )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(testUser))
        `when`(noteRepository.save(any(Note::class.java))).thenReturn(noteWithExpiration)

        //when
        val result = noteService.createNote(testUser.id, noteRequestWithExpiration)

        //then
        verify(noteRepository).save(notesCaptor.capture())
        val capturedNote = notesCaptor.value
        assertEquals(capturedNote.expirationTime, noteWithExpiration.expirationTime)
        // Verify the returned result
        Assertions.assertNotNull(result)
        assertEquals(noteWithExpiration.id, result.id)
    }

    @Test
    fun `createNote should throw exception when user not found`() {
        //given
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        //when
        val exception = assertThrows(EntityNotFoundException::class.java) {
            noteService.createNote(1L, noteRequest)
        }

        //then
        assertEquals("User not found", exception.message)
        verify(userRepository, times(1)).findById(1L)
        verify(noteRepository, never()).save(any(Note::class.java))
    }

    @Test
    fun `getNoteById should return note when note exists and user has access`() {
        // Arrange
        `when`(noteRepository.findByIdAndUserId(1L, 1L)).thenReturn(testNote)

        // Act
        val result = noteService.getNoteById(1L, 1L)

        // Assert
        assertNotNull(result)
        assertEquals(testNote.id, result.id)
        assertEquals(testNote.title, result.title)
        assertEquals(testNote.content, result.content)
        assertEquals(testUser, result.user)

        verify(noteRepository, times(1)).findByIdAndUserId(1L, 1L)
    }

    @Test
    fun `getNoteById should throw exception when note not found or user has no access`() {
        // Arrange
        `when`(noteRepository.findByIdAndUserId(1L, 1L)).thenReturn(null)

        // Act & Assert
        val exception = assertThrows(EntityNotFoundException::class.java) {
            noteService.getNoteById(1L, 1L)
        }

        assertEquals("Note not found or you don't have access", exception.message)
        verify(noteRepository, times(1)).findByIdAndUserId(1L, 1L)
    }
}