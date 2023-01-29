package com.mecofarid.trending.domain.features.trending.data.mapper

import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity.OwnerLocalEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity.OwnerRemoteEntity

class TrendingRemoteEntityToLocalTrendingEntityMapper(
    private val ownerMapper: Mapper<OwnerRemoteEntity, OwnerLocalEntity>
): Mapper<TrendingRemoteEntity, TrendingLocalEntity> {

    override fun map(input: TrendingRemoteEntity): TrendingLocalEntity =
        with(input){
            TrendingLocalEntity(
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