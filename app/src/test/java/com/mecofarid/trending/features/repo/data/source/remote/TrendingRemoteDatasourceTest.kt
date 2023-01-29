package com.mecofarid.trending.features.repo.data.source.remote

import com.mecofarid.test.feature.repo.anyRepoRemoteEntity
import com.mecofarid.trending.anyList
import com.mecofarid.trending.domain.common.data.NetworkException
import com.mecofarid.trending.domain.common.data.Query
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.data.source.remote.TRENDING_QUERY
import com.mecofarid.trending.domain.features.trending.data.source.remote.TrendingRemoteDatasource
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingResponseRemoteEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.service.TrendingService
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

internal class TrendingRemoteDatasourceTest{

    @MockK
    private lateinit var trendingService: TrendingService

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
        coEvery { trendingService.searchTrending(any()) } returns TrendingResponseRemoteEntity(expectedEntityList)

        val actualEntityList = datasource.get(query = GetAllTrendingsQuery)

        assertEquals(expectedEntityList, actualEntityList)
        coVerify(exactly = 1) { trendingService.searchTrending(TRENDING_QUERY) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data exception rethrown when network exception is thrown`() = runTest {
        val expectedException = setOf(
            NetworkException.HttpException(code = randomInt(min = 300, max = 599)),
            NetworkException.ConnectionException()
        ).random()
        val datasource = givenDataSource()
        coEvery { trendingService.searchTrending(any()) } throws expectedException

        val actualException =
            try {
                datasource.get(query = GetAllTrendingsQuery)
                null
            } catch (e: NetworkException) {
                e
            }

        assertEquals(expectedException, actualException)
        coVerify(exactly = 1) { trendingService.searchTrending(TRENDING_QUERY) }
    }

    private fun givenDataSource() = TrendingRemoteDatasource(trendingService)
}
