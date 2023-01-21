package com.mecofarid.trending.features.repo.data.mapper

import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity.OwnerLocalEntity
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoRemoteEntity.OwnerRemoteEntity

class RepoRemoteEntityToLocalEntityMapper(
    private val ownerMapper: Mapper<OwnerRemoteEntity, OwnerLocalEntity>
): Mapper<RepoRemoteEntity, RepoLocalEntity> {

    override fun map(input: RepoRemoteEntity): RepoLocalEntity =
        with(input){
            RepoLocalEntity(
                name,
                language,
                stargazersCount,
                description,
                ownerMapper.map(owner)
            )
        }
}

class OwnerRemoteEntityToOwnerLocalEntityMapper: Mapper<OwnerRemoteEntity, OwnerLocalEntity> {

    override fun map(input: OwnerRemoteEntity): OwnerLocalEntity = OwnerLocalEntity(input.login, input.avatarUrl)
}
