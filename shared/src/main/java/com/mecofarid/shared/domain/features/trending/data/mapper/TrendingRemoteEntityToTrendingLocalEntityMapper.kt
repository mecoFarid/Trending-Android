package com.mecofarid.shared.domain.features.trending.data.mapper

import com.mecofarid.shared.domain.common.data.Mapper
import com.mecofarid.shared.domain.features.trending.data.source.remote.service.OwnerRemoteEntity
import com.mecofarid.shared.domain.features.trending.data.source.remote.service.TrendingRemoteEntity
import com.mecofarid.shared.domain.features.trending.domain.model.Trending


class TrendingRemoteEntityToTrendingMapper(
    private val ownerMapper: Mapper<OwnerRemoteEntity, Trending.Owner>
): Mapper<TrendingRemoteEntity, Trending> {

    override fun map(input: TrendingRemoteEntity): Trending =
        with(input){
            Trending(
                id,
                name,
                primaryLanguage?.name,
                stargazers.totalCount,
                description,
                ownerMapper.map(owner)
            )
        }
}

class OwnerRemoteEntityToOwnerMapper:
    Mapper<OwnerRemoteEntity, Trending.Owner> {

    override fun map(input: OwnerRemoteEntity): Trending.Owner =
        Trending.Owner(input.login, input.avatarUrl)
}
