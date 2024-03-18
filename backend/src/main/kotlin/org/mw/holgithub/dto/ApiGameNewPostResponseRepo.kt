package org.mw.holgithub.dto

data class ApiGameNewPostResponseReposPart(
    val first: ApiGameNewPostResponseRepo,
    val second: ApiGameNewPostResponseRepo,
)

data class ApiGameNewPostResponseRepo(
    val name: String,
    val description: String,
)
