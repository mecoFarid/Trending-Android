package com.mecofarid.shared.domain.features.trending.data.mapper

import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.shared.domain.features.trending.domain.model.Trending

class TrendingToTrendingLocalEntityMapper(
    private val ownerMapper: Mapper<Trending.Owner, TrendingLocalEntity.OwnerLocalEntity>
): Mapper<Trending, TrendingLocalEntity> {

    override fun map(input: Trending): TrendingLocalEntity =
        with(input) {
            TrendingLocalEntity(
                name,
                language,
                stargazersCount,
                description,
                ownerMapper.map(owner)
            )
        }
}

class OwnerToOwnerLocalEntityMapper:
    Mapper<Trending.Owner, TrendingLocalEntity.OwnerLocalEntity> {
    override fun map(input: Trending.Owner): TrendingLocalEntity.OwnerLocalEntity =
        TrendingLocalEntity.OwnerLocalEntity(input.login, input.avatarUrl)
}
