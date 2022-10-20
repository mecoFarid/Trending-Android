package com.mecofarid.sadapayallstar.features.repo.data.mapper

import com.mecofarid.sadapayallstar.common.data.Mapper
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.OwnerLocalEntity
import com.mecofarid.sadapayallstar.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.sadapayallstar.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.sadapayallstar.features.repo.data.source.remote.entity.RepoRemoteEntity.OwnerRemoteEntity

class RepoRemoteEntityToLocalEntityMapper(
    private val ownerMapper: Mapper<OwnerRemoteEntity, OwnerLocalEntity>
): Mapper<List<RepoRemoteEntity>, List<RepoLocalEntity>> {

    override fun map(input: List<RepoRemoteEntity>): List<RepoLocalEntity> =
        input.map {
            RepoLocalEntity(
                it.name,
                it.language,
                it.stargazers_count,
                it.description,
                ownerMapper.map(it.owner)
            )
        }
}

class OwnerRemoteEntityToOwnerLocalEntityMapper: Mapper<OwnerRemoteEntity, OwnerLocalEntity> {

    override fun map(input: OwnerRemoteEntity): OwnerLocalEntity = OwnerLocalEntity(input.login, input.avatar_url)
}