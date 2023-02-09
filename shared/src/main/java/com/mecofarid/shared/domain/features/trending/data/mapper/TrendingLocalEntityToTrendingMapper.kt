package com.mecofarid.shared.domain.features.trending.data.mapper

import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.shared.domain.features.trending.domain.model.Trending

class TrendingLocalEntityToTrendingMapper(
    private val ownerMapper: Mapper<TrendingLocalEntity.OwnerLocalEntity, Trending.Owner>
): Mapper<TrendingLocalEntity, Trending> {

    override fun map(input: TrendingLocalEntity): Trending =
        with(input) {
            Trending(
                id,
                name,
                language,
                stargazersCount,
                description,
                ownerMapper.map(owner)
            )
        }
}

class OwnerLocalEntityToOwnerMapper:
    Mapper<TrendingLocalEntity.OwnerLocalEntity, Trending.Owner> {

    override fun map(input: TrendingLocalEntity.OwnerLocalEntity): Trending.Owner =
        Trending.Owner(input.login, input.avatarUrl)
}
