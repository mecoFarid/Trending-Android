package com.mecofarid.trending.domain.features.trending.data.mapper

import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity.OwnerLocalEntity
import com.mecofarid.trending.domain.features.trending.domain.model.Trending
import com.mecofarid.trending.domain.features.trending.domain.model.Trending.Owner

class TrendingLocalEntityToTrendingMapper(
    private val ownerMapper: Mapper<OwnerLocalEntity, Owner>
): Mapper<TrendingLocalEntity, Trending> {

    override fun map(input: TrendingLocalEntity): Trending =
        with(input) {
            Trending(
                name,
                language,
                stargazersCount,
                description,
                ownerMapper.map(owner)
            )
        }
}

class OwnerLocalEntityToOwnerMapper: Mapper<OwnerLocalEntity, Owner> {

    override fun map(input: OwnerLocalEntity): Owner = Owner(input.login, input.avatarUrl)
}