package com.mecofarid.trending.features.repo.data.source.remote.entity

data class RepoResponseRemoteEntity(val items: List<RepoRemoteEntity>?)

data class RepoRemoteEntity(
    val name: String,
    val language: String,
    val stargazers_count: Long,
    val description: String,
    val owner: OwnerRemoteEntity
){
    data class OwnerRemoteEntity(
        val login: String,
        val avatar_url: String
    )
}