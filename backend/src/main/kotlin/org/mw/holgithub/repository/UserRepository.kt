package org.mw.holgithub.repository

import org.mw.holgithub.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserModel, UUID>