package com.mecofarid.trending.features.repo.domain.model

data class Repo(
    val name: String,
    val language: String,
    val stargazersCount: Long,
    val description: String,
    val owner: Owner
){
    data class Owner(
        val login: String,
        val avatarUrl: String
    )
}
