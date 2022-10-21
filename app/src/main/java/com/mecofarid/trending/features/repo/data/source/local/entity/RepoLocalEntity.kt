package com.mecofarid.trending.features.repo.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepoLocalEntity(
    val name: String,
    val language: String,
    val stargazersCount: Long,
    val description: String,
    @Embedded val owner: OwnerLocalEntity,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
){
    data class OwnerLocalEntity(
        val login: String,
        val avatarUrl: String,
    )
}
