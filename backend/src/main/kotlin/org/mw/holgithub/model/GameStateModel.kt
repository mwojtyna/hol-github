package org.mw.holgithub.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "game_state")
data class GameStateModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne(targetEntity = RepoModel::class)
    @JoinColumn(name = "first_repo_id", nullable = false)
    var firstRepo: RepoModel,

    @OneToOne(targetEntity = RepoModel::class)
    @JoinColumn(name = "second_repo_id", nullable = false)
    var secondRepo: RepoModel,
)
