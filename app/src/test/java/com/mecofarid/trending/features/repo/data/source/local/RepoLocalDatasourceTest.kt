package com.mecofarid.trending.features.repo.data.source.local

import com.mecofarid.trending.anyList
import com.mecofarid.trending.common.data.Query
import com.mecofarid.trending.features.repo.anyRepoLocalEntity
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.data.source.local.dao.RepoLocalEntityDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class RepoLocalDatasourceTest {

    @MockK
    private lateinit var repoLocalEntityDao: RepoLocalEntityDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct data returned when query is valid`() = runTest {
        val expectedEntityList = anyList { anyRepoLocalEntity() }
        val datasource = givenDataSource()
        coEvery { repoLocalEntityDao.getAllTrendingRepos() } returns expectedEntityList

        val actualEntityList = datasource.get(query = GetAllTrendingReposQuery)

        assertEquals(expectedEntityList, actualEntityList)
        coVerify(exactly = 1) { repoLocalEntityDao.getAllTrendingRepos() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = UnsupportedOperationException::class)
    fun `assert exception is thrown when trying to get data with invalid query`() = runTest {
        val datasource = givenDataSource()
        val query: Query = object : Query {}
        datasource.get(query = query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct data persisted when query is valid`() = runTest {
        val expectedEntityList = anyList { anyRepoLocalEntity() }
        val datasource = givenDataSource()
        coJustRun { repoLocalEntityDao.deleteAllTrendingReposAndInsert(expectedEntityList) }

        val actualEntityList = datasource.put(query = GetAllTrendingReposQuery, expectedEntityList)

        assertEquals(expectedEntityList, actualEntityList)
        coVerify(exactly = 1) { repoLocalEntityDao.deleteAllTrendingReposAndInsert(expectedEntityList) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = UnsupportedOperationException::class)
    fun `assert exception is thrown when trying to put data with invalid query`() = runTest {
        val entityList = anyList { anyRepoLocalEntity() }
        val datasource = givenDataSource()
        val query: Query = object : Query {}
        datasource.put(query = query, entityList)
    }

    private fun givenDataSource() = RepoLocalDatasource(repoLocalEntityDao)
}