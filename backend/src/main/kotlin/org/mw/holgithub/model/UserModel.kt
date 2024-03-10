package org.mw.holgithub.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user")
class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @Column(name = "username", unique = true, nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    val password: String,
)