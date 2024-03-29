package com.mecofarid.shared.domain.features.trending.data.mapper

import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.shared.domain.features.trending.domain.model.Trending


class TrendingRemoteEntityToTrendingMapper(
    private val ownerMapper: Mapper<TrendingRemoteEntity.OwnerRemoteEntity, Trending.Owner>
): Mapper<TrendingRemoteEntity, Trending> {

    override fun map(input: TrendingRemoteEntity): Trending =
        with(input){
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

class OwnerRemoteEntityToOwnerMapper:
    Mapper<TrendingRemoteEntity.OwnerRemoteEntity, Trending.Owner> {

    override fun map(input: TrendingRemoteEntity.OwnerRemoteEntity): Trending.Owner =
        Trending.Owner(input.login, input.avatarUrl)
}
