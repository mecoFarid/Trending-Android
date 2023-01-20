package com.mecofarid.trending.features.repo.data.source.remote

import com.mecofarid.trending.anyList
import com.mecofarid.test.feature.repo.anyRepoRemoteEntity
import com.mecofarid.trending.common.data.*
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoResponseRemoteEntity
import com.mecofarid.trending.features.repo.data.source.remote.service.RepoService
import com.mecofarid.trending.randomInt
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class RepoRemoteDatasourceTest{

    @MockK
    private lateinit var repoService: RepoService

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = UnsupportedOperationException::class)
    fun `throw exception when trying to put data`() = runTest {
        val datasource = givenDataSource()
        val entityList = anyList { anyRepoRemoteEntity() }
        val query: Query = object : Query {}
        datasource.put(query, entityList)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = UnsupportedOperationException::class)
    fun `assert exception is thrown when trying to get data with invalid query`() = runTest {
        val datasource = givenDataSource()
        val query: Query = object : Query {}
        datasource.get(query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct data returned when query is valid`() = runTest {
        val expectedEntityList = anyList { anyRepoRemoteEntity() }
        val datasource = givenDataSource()
        coEvery { repoService.searchRepos(any()) } returns RepoResponseRemoteEntity(expectedEntityList)

        val actualEntityList = datasource.get(query = GetAllTrendingReposQuery)

        assertEquals(expectedEntityList, actualEntityList)
        coVerify(exactly = 1) { repoService.searchRepos(TRENDING_REPOS_QUERY) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data exception rethrown when network exception is thrown`() = runTest {
        val expectedException = setOf(
            NetworkException.HttpException(code = randomInt(min = 300, max = 599)),
            NetworkException.ConnectionException()
        ).random()
        val datasource = givenDataSource()
        coEvery { repoService.searchRepos(any()) } throws expectedException

        val actualException =
            try {
                datasource.get(query = GetAllTrendingReposQuery)
                null
            } catch (e: NetworkException) {
                e
            }

        assertEquals(expectedException, actualException)
        coVerify(exactly = 1) { repoService.searchRepos(TRENDING_REPOS_QUERY) }
    }

    private fun givenDataSource() = RepoRemoteDatasource(repoService)
}