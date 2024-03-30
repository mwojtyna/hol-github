package org.mw.holgithub.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RepoDto(
    val name: String,
    val description: String,
    @field:JsonProperty("star_amount") val starAmount: Int?,
)
