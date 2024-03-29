package com.mecofarid.shared.cache

import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.NetworkException
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.data.datasource.Datasource
import com.mecofarid.shared.domain.common.data.repository.cache.CacheRepository
import com.mecofarid.shared.domain.common.either.Either
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import com.mecofarid.test.anyList
import com.mecofarid.test.feature.repo.anyTrending
import com.mecofarid.test.randomInt
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


internal class CacheRepositoryTest {

    @MockK
    private lateinit var cacheDatasource: Datasource<List<Trending>, DataException>

    @MockK
    private lateinit var mainDatasource: Datasource<List<Trending>, DataException>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main and cached when sync-main is requested`() = runTest {
        val cacheData = Either.Right(anyList { anyTrending() })
        val mainData = Either.Right(anyList { anyTrending() })
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        coEvery { mainDatasource.get(query) } returns mainData
        coEvery { cacheDatasource.put(query, mainData.value) } returns cacheData
        coEvery { cacheDatasource.get(query) } returns cacheData

        val actualData  = repository.get(query, Operation.SyncMainOperation)

        assertEquals(cacheData, actualData)
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.put(query, mainData.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when sync-main is requested and main data source fails`() = runTest {
        val cacheData =
            listOf(
                Either.Right(anyList { anyTrending() }),
                Either.Left(DataException.DataNotFoundException())
            ).random()
        val mainException = listOf(
            NetworkException.HttpException(randomInt()),
            NetworkException.ConnectionException()
        ).random()
        val mainData = Either.Left(mainException)
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        coEvery { mainDatasource.get(query) } returns mainData
        coEvery { cacheDatasource.get(query) } returns cacheData

        val actualData = repository.get(query, Operation.SyncMainOperation)

        assertEquals(cacheData, actualData)
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 1) { cacheDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when cache-else-main-sync is requested`() = runTest {
        val successCacheData = Either.Right(anyList { anyTrending() })

        val cacheData =
            listOf(
                Either.Right(anyList { anyTrending() }),
                Either.Left(DataException.DataNotFoundException())
            ).random()
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        coEvery { cacheDatasource.get(query) } returns successCacheData andThen cacheData

        val actualData = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(actualData, actualData)
        coVerify(exactly = 1) { cacheDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 0) { mainDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main when cache-else-main-sync is requested but cache data source fails`() = runTest {
        val cacheDataInitial = Either.Left(DataException.DataNotFoundException())
        val mainData = Either.Right(anyList { anyTrending() })
        val cacheDataAfterCache = Either.Right(anyList { anyTrending() })
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        coEvery { cacheDatasource.get(query) } returns cacheDataInitial
        coEvery { mainDatasource.get(query) } returns mainData
        coEvery { cacheDatasource.put(query, mainData.value) } returns cacheDataAfterCache

        val actualData  = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(actualData, actualData)
        coVerify(exactly = 2) { cacheDatasource.get(query) }
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.put(query, mainData.value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert error is returned when both data sources fail when main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        val expectedData = Either.Left(DataException.DataNotFoundException())
        coEvery { cacheDatasource.get(query) } returns expectedData
        coEvery { mainDatasource.get(query) } returns Either.Left(DataException.DataNotFoundException())

        val actualData  = repository.get(query, Operation.SyncMainOperation)
        
        assertEquals(actualData, expectedData)
        coVerify(exactly = 1) { cacheDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 1) { mainDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert error is returned when both data sources fail when cache-else-main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        val expectedData = Either.Left(DataException.DataNotFoundException())
        coEvery { cacheDatasource.get(query) } returns expectedData
        coEvery { mainDatasource.get(query) } returns Either.Left(DataException.DataNotFoundException())

        val actualData  = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(actualData, expectedData)
        coVerify(exactly = 2) { cacheDatasource.get(query) }
        coVerify(exactly = 0) { cacheDatasource.put(any(), any()) }
        coVerify(exactly = 1) { mainDatasource.get(query) }
    }

    private fun givenRepository() = CacheRepository(
        cacheDatasource,
        mainDatasource
    )
}
