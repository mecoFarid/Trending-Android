package com.mecofarid.sadapayallstar.features.repo.domain.model

data class Repo(
    private val name: String,
    private val language: String,
    private val stargazersCount: Long,
    private val description: String,
    private val owner: Owner
){
    data class Owner(
        private val login: String,
        private val avatarUrl: String
    )
}
