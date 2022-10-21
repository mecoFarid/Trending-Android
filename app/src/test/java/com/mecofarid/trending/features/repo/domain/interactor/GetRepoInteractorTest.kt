package com.mecofarid.trending.features.repo.domain.interactor

import com.mecofarid.trending.anyList
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.common.data.Repository
import com.mecofarid.test.feature.repo.anyRepo
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.domain.model.Repo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetRepoInteractorTest(){

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct result returned`() = runTest {
        val expectedRepoList = anyList { anyRepo() }
        val repository: Repository<List<Repo>> = mockk()
        val interactor = GetRepoInteractor(repository)
        val operation = setOf(
            Operation.SyncMainOperation,
            Operation.CacheElseSyncMainOperation
        ).random()
        coEvery { repository.get(GetAllTrendingReposQuery, operation) } returns expectedRepoList

        val actualRepoList = interactor(GetAllTrendingReposQuery, operation)

        assertEquals(expectedRepoList, actualRepoList)
        coVerify(exactly = 1) { interactor(GetAllTrendingReposQuery, operation) }
    }
}