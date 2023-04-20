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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


internal class CacheRepositoryTest {

    @Mock
    private lateinit var cacheDatasource: Datasource<List<Trending>, DataException>

    @Mock
    private lateinit var mainDatasource: Datasource<List<Trending>, DataException>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main and cached when sync-main is requested`() = runTest {
        val cacheData = Either.Right(anyList { anyTrending() })
        val mainData = Either.Right(anyList { anyTrending() })
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        
        whenever(mainDatasource.get(query))
            .thenReturn(mainData)
        whenever(cacheDatasource.put(query, mainData.value))
            .thenReturn(cacheData)
        whenever(cacheDatasource.get(query))
            .thenReturn(cacheData)

        val actualData = repository.get(query, Operation.SyncMainOperation)

        assertEquals(cacheData, actualData)
        verify(mainDatasource, times(1))
            .get(query)
        verify(cacheDatasource, times(1))
            .get(query)
        verify(cacheDatasource, times(1))
            .put(query, mainData.value)
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
        whenever(mainDatasource.get(query))
            .thenReturn(mainData)
        whenever(cacheDatasource.get(query))
            .thenReturn(cacheData)

        val actualData = repository.get(query, Operation.SyncMainOperation)

        assertEquals(cacheData, actualData)
        verify(mainDatasource, times(1))
            .get(query)
        verify(cacheDatasource, never())
            .put(any(), any())
        verify(cacheDatasource, times(1))
            .get(query)
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
        whenever(cacheDatasource.get(query))
            .thenReturn(successCacheData)
            .thenReturn(cacheData)

        val actualData = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(actualData, actualData)
        verify(cacheDatasource, times(1))
            .get(query)
        verify(cacheDatasource, never())
            .put(any(), any())
        verify(mainDatasource, never())
            .get(query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main when cache-else-main-sync is requested but cache data source fails`() = runTest {
        val cacheDataInitial = Either.Left(DataException.DataNotFoundException())
        val mainData = Either.Right(anyList { anyTrending() })
        val cacheDataAfterCache = Either.Right(anyList { anyTrending() })
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        whenever(cacheDatasource.get(query))
            .thenReturn(cacheDataInitial)
        whenever(mainDatasource.get(query))
            .thenReturn(mainData)
        whenever(cacheDatasource.put(query, mainData.value))
            .thenReturn(cacheDataAfterCache)

        val actualData = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(actualData, actualData)
        verify(cacheDatasource, times(2))
            .get(query)
        verify(mainDatasource, times(1))
            .get(query)
        verify(cacheDatasource, times(1))
            .put(query, mainData.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert error is returned when both data sources fail when main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        val expectedData = Either.Left(DataException.DataNotFoundException())
        whenever(cacheDatasource.get(query))
            .thenReturn(expectedData)
        whenever(mainDatasource.get(query))
            .thenReturn(Either.Left(DataException.DataNotFoundException()))

        val actualData = repository.get(query, Operation.SyncMainOperation)

        assertEquals(actualData, expectedData)
        verify(cacheDatasource, times(1))
            .get(query)
        verify(cacheDatasource, never())
            .put(any(), any())
        verify(mainDatasource, times(1))
            .get(query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert error is returned when both data sources fail when cache-else-main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        val expectedData = Either.Left(DataException.DataNotFoundException())
        whenever(cacheDatasource.get(query))
            .thenReturn(expectedData)
        whenever(mainDatasource.get(query))
            .thenReturn(Either.Left(DataException.DataNotFoundException()))

        val actualData = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(actualData, expectedData)
        verify(cacheDatasource, times(2))
            .get(query)
        verify(cacheDatasource, never())
            .put(any(), any())
        verify(mainDatasource, times(1))
            .get(query)
    }

    private fun givenRepository() = CacheRepository(
        cacheDatasource,
        mainDatasource
    )
}
