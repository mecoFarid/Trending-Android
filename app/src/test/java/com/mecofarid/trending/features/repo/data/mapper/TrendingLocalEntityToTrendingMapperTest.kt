package com.mecofarid.trending.features.repo.data.mapper

import com.mecofarid.test.feature.repo.anyOwner
import com.mecofarid.test.feature.repo.anyOwnerLocalEntity
import com.mecofarid.test.feature.repo.anyRepoLocalEntity
import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.features.trending.data.mapper.OwnerLocalEntityToOwnerMapper
import com.mecofarid.trending.domain.features.trending.data.mapper.TrendingLocalEntityToTrendingMapper
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.trending.domain.features.trending.domain.model.Trending
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

internal class TrendingLocalEntityToTrendingMapperTest{

    @Test
    fun `assert repo mapper returns correct result`() {
        val ownerMapper: Mapper<TrendingLocalEntity.OwnerLocalEntity, Trending.Owner> = mockk()
        val repoEntity = anyRepoLocalEntity()
        val repoMapper = TrendingLocalEntityToTrendingMapper(ownerMapper)
        every { ownerMapper.map(repoEntity.owner) } returns anyOwner()

        val repo = repoMapper.map(repoEntity)

        repoEntity.apply {
            assertEquals(name, repo.name)
            assertEquals(language, repo.language)
            assertEquals(stargazersCount, repo.stargazersCount)
            assertEquals(description, repo.description)
        }
        verify(exactly = 1) { ownerMapper.map(repoEntity.owner) }
    }

    @Test
    fun `assert owner mapper returns correct result`() {
        val ownerEntity = anyOwnerLocalEntity()
        val ownerMapper = OwnerLocalEntityToOwnerMapper()

        val owner = ownerMapper.map(ownerEntity)

        ownerEntity.apply {
            assertEquals(login, owner.login)
            assertEquals(avatarUrl, owner.avatarUrl)
        }
    }
}
