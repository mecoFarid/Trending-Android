package com.mecofarid.trending.features.repo.domain.interactor

import com.mecofarid.test.feature.repo.anyRepo
import com.mecofarid.trending.anyList
import com.mecofarid.trending.domain.common.data.Operation
import com.mecofarid.trending.domain.common.data.Repository
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.trending.domain.features.trending.domain.model.Trending
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetTrendingInteractorTest{

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct result returned`() = runTest {
        val expectedRepoList = anyList { anyRepo() }
        val repository: Repository<List<Trending>> = mockk()
        val interactor = GetTrendingInteractor(repository)
        val operation = setOf(
            Operation.SyncMainOperation,
            Operation.CacheElseSyncMainOperation
        ).random()
        coEvery { repository.get(GetAllTrendingsQuery, operation) } returns expectedRepoList

        val actualRepoList = interactor(GetAllTrendingsQuery, operation)

        assertEquals(expectedRepoList, actualRepoList)
        coVerify(exactly = 1) { interactor(GetAllTrendingsQuery, operation) }
    }
}
