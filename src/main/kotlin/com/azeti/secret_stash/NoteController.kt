package com.azeti.secret_stash

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notes")
class NoteController (
    private val noteService: NoteService
)