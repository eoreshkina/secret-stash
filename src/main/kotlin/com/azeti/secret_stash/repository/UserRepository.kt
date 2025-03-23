package com.azeti.secret_stash.repository

import com.azeti.secret_stash.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}