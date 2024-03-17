package org.mw.holgithub.repository

import org.mw.holgithub.model.RepoModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RepoRepository : JpaRepository<RepoModel, UUID>