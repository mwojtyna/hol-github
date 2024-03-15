package org.mw.holgithub.repository

import org.mw.holgithub.model.SessionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SessionRepository : JpaRepository<SessionModel, UUID>