package com.mecofarid.sadapayallstar.features.repo.data.remote.entity

data class RepoRemoteEntity(
    private val name: String,
    private val language: String,
    private val stargazers_count: Long,
    private val description: String,
    private val owner: Owner
){
    data class Owner(
        private val login: String,
        private val avatar_url: String
    )
}