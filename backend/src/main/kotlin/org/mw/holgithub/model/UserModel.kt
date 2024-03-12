package org.mw.holgithub.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user")
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    val id: UUID? = null,

    @Column(name = "username", unique = true, nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    val password: String,
)