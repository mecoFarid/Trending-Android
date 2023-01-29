package com.mecofarid.trending.features.repo.data.mapper

import com.mecofarid.test.feature.repo.anyOwnerLocalEntity
import com.mecofarid.test.feature.repo.anyOwnerRemoteEntity
import com.mecofarid.test.feature.repo.anyRepoRemoteEntity
import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.features.trending.data.mapper.OwnerRemoteEntityToOwnerLocalEntityMapper
import com.mecofarid.trending.domain.features.trending.data.mapper.TrendingRemoteEntityToLocalTrendingEntityMapper
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

internal class TrendingRemoteEntityToLocalTrendingEntityMapperTest{

    @Test
    fun `assert repo mapper returns correct result`() {
        val ownerMapper: Mapper<TrendingRemoteEntity.OwnerRemoteEntity, TrendingLocalEntity.OwnerLocalEntity> = mockk()
        val repoEntity = anyRepoRemoteEntity()
        val repoMapper = TrendingRemoteEntityToLocalTrendingEntityMapper(ownerMapper)
        every { ownerMapper.map(repoEntity.owner) } returns anyOwnerLocalEntity()

        val repo = repoMapper.map(repoEntity)

        repoEntity.apply {
            Assert.assertEquals(name, repo.name)
            Assert.assertEquals(language, repo.language)
            Assert.assertEquals(stargazersCount, repo.stargazersCount)
            Assert.assertEquals(description, repo.description)
        }
        verify(exactly = 1) { ownerMapper.map(repoEntity.owner) }
    }

    @Test
    fun `assert owner mapper returns correct result`() {
        val ownerEntity = anyOwnerRemoteEntity()
        val ownerMapper = OwnerRemoteEntityToOwnerLocalEntityMapper()

        val owner = ownerMapper.map(ownerEntity)

        ownerEntity.apply {
            Assert.assertEquals(login, owner.login)
            Assert.assertEquals(avatarUrl, owner.avatarUrl)
        }
    }
}
