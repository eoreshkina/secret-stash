package com.azeti.secret_stash.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notes",
    indexes = [
        Index(name = "idx_note_user_id", columnList = "user_id"),
        Index(name = "idx_note_created_at", columnList = "created_at"),
        Index(name = "idx_note_expiration_time", columnList = "expiration_time")
    ]
)
data class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "expiration_time")
    var expirationTime: LocalDateTime? = null,

    @Column(nullable = false)
    var version: Int = 1
)