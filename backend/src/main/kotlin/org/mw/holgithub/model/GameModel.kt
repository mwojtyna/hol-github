package org.mw.holgithub.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "game")
data class GameModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @ManyToOne(targetEntity = UserModel::class)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserModel,

    @Column(name = "score", nullable = false)
    val score: Int,
)