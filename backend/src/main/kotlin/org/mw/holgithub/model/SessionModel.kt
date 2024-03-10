package org.mw.holgithub.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "session")
data class SessionModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @ManyToOne(targetEntity = UserModel::class)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserModel,

    @Column(name = "expire_date", nullable = false)
    val expireDate: Timestamp
)