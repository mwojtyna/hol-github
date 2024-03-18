package org.mw.holgithub.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "repo")
data class RepoModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "text")
    val name: String,

    @Column(name = "description", nullable = false, columnDefinition = "text")
    val description: String,

    @Column(name = "star_amount", nullable = false)
    val starAmount: Int,

    @Column(name = "image", nullable = false, columnDefinition = "bytea")
    val image: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RepoModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (starAmount != other.starAmount) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + starAmount
        result = 31 * result + image.contentHashCode()
        return result
    }
}