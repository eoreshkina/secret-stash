package com.azeti.secret_stash.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "note_versions",
    indexes = [
        Index(name = "idx_note_version_note_id", columnList = "note_id"),
        Index(name = "idx_note_version_version", columnList = "version")
    ]
)
data class NoteVersion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    val note: Note,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(nullable = false)
    val version: Int,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)