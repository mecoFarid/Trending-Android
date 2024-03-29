package com.mecofarid.shared.domain.features.trending.data.source.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingResponseRemoteEntity(val items: List<TrendingRemoteEntity>)

@Serializable
data class TrendingRemoteEntity(
    val id: Long,
    val name: String,
    val language: String?,
    @SerialName("stargazers_count")
    val stargazersCount: Long,
    val description: String?,
    val owner: OwnerRemoteEntity
){
    @Serializable
    data class OwnerRemoteEntity(
        val login: String,
        @SerialName("avatar_url")
        val avatarUrl: String?
    )
}
