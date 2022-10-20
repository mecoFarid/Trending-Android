package com.mecofarid.sadapayallstar.features.repo.data.local.entity

data class RepoLocalEntity(
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