package com.mecofarid.trending.features.repo.data.source.local

import com.mecofarid.test.feature.repo.anyRepoLocalEntity
import com.mecofarid.trending.anyList
import com.mecofarid.trending.domain.common.data.DataException
import com.mecofarid.trending.domain.common.data.Query
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.data.source.local.TrendingLocalDatasource
import com.mecofarid.trending.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class TrendingLocalDatasourceTest {

    @MockK
    private lateinit var trendingLocalEntityDao: TrendingLocalEntityDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct data returned when query is valid`() = runTest {
        val expectedEntityList = anyList { anyRepoLocalEntity() }
        val datasource = givenDataSource()
        coEvery { trendingLocalEntityDao.getAllTrendings() } returns expectedEntityList

        val actualEntityList = datasource.get(query = GetAllTrendingsQuery)

        assertEquals(expectedEntityList, actualEntityList)
        coVerify(exactly = 1) { trendingLocalEntityDao.getAllTrendings() }
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
        coJustRun { trendingLocalEntityDao.deleteAllTrendingAndInsert(expectedEntityList) }

        val actualEntityList = datasource.put(query = GetAllTrendingsQuery, expectedEntityList)

        assertEquals(expectedEntityList, actualEntityList)
        coVerify(exactly = 1) { trendingLocalEntityDao.deleteAllTrendingAndInsert(expectedEntityList) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = UnsupportedOperationException::class)
    fun `assert exception is thrown when trying to put data with invalid query`() = runTest {
        val entityList = anyList { anyRepoLocalEntity() }
        val datasource = givenDataSource()
        val query: Query = object : Query {}
        datasource.put(query = query, entityList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = DataException.DataNotFoundException::class)
    fun `assert exception is thrown when no data found`() = runTest {
        val datasource = givenDataSource()
        coEvery { trendingLocalEntityDao.getAllTrendings() } returns emptyList()

        datasource.get(GetAllTrendingsQuery)
    }

    private fun givenDataSource() = TrendingLocalDatasource(trendingLocalEntityDao)
}
