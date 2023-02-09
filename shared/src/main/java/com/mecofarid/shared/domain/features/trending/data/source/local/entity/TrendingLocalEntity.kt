package com.mecofarid.shared.domain.features.trending.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrendingLocalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val language: String?,
    val stargazersCount: Long,
    val description: String?,
    @Embedded val owner: OwnerLocalEntity
){
    data class OwnerLocalEntity(
        val login: String,
        val avatarUrl: String?,
    )
}
