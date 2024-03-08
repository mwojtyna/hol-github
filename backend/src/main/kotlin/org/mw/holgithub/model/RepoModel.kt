package org.mw.holgithub.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "repo")
class RepoModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @Column(name = "name", unique = true, nullable = false)
    val name: String,

    @Column(name = "star_amount", nullable = false)
    val starAmount: Int,

    @Lob
    @Column(name = "image", nullable = false)
    val image: Byte,
)