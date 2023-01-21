package com.mecofarid.trending.features.repo.data.mapper

import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.test.feature.repo.anyOwner
import com.mecofarid.test.feature.repo.anyOwnerLocalEntity
import com.mecofarid.test.feature.repo.anyRepoLocalEntity
import com.mecofarid.trending.domain.features.repo.data.mapper.OwnerLocalEntityToOwnerMapper
import com.mecofarid.trending.domain.features.repo.data.mapper.RepoLocalEntityToRepoMapper
import com.mecofarid.trending.domain.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.domain.features.repo.domain.model.Repo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

internal class RepoLocalEntityToRepoMapperTest{

    @Test
    fun `assert repo mapper returns correct result`() {
        val ownerMapper: Mapper<RepoLocalEntity.OwnerLocalEntity, Repo.Owner> = mockk()
        val repoEntity = anyRepoLocalEntity()
        val repoMapper = RepoLocalEntityToRepoMapper(ownerMapper)
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
