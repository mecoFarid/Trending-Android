package com.mecofarid.shared.domain.features.trending.domain.model

data class Trending(
    val name: String,
    val language: String?,
    val stargazersCount: Long,
    val description: String?,
    val owner: Owner
){
    data class Owner(
        val login: String,
        val avatarUrl: String?
    )
}
