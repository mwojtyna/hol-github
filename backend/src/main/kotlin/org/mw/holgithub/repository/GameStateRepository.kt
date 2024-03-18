package org.mw.holgithub.repository

import org.mw.holgithub.model.GameStateModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GameStateRepository : JpaRepository<GameStateModel, UUID>