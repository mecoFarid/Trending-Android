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
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
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

    @Test
    fun `assert data fetched from main and cached when sync-main is requested`() {
        val cacheData = Either.Right(anyList { anyTrending() })
        val mainData = Either.Right(anyList { anyTrending() })
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        every { mainDatasource.get(query) } returns Flowable.just(mainData)
        every { cacheDatasource.put(query, mainData.value) } returns Flowable.just(cacheData)
        every { cacheDatasource.get(query) } returns Flowable.just(cacheData)

        val actualData  = repository.get(query, Operation.SyncMainOperation).blockingFirst()

        assertEquals(cacheData, actualData)
        verify(exactly = 1) { mainDatasource.get(query) }
        verify(exactly = 1) { cacheDatasource.get(query) }
        verify(exactly = 1) { cacheDatasource.put(query, mainData.value) }
    }

    @Test
    fun `assert data fetched from cache when sync-main is requested and main data source fails`() {
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
        every { mainDatasource.get(query) } returns Flowable.just(mainData)
        every { cacheDatasource.get(query) } returns Flowable.just(cacheData)

        val actualData = repository.get(query, Operation.SyncMainOperation).blockingFirst()

        assertEquals(cacheData, actualData)
        verify(exactly = 1) { mainDatasource.get(query) }
        verify(exactly = 0) { cacheDatasource.put(any(), any()) }
        verify(exactly = 1) { cacheDatasource.get(query) }
    }

    @Test
    fun `assert data fetched from cache when cache-else-main-sync is requested`() {
        val expectedData = Either.Right(anyList { anyTrending() })
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        every { cacheDatasource.get(query) } returns Flowable.just(expectedData)

        val actualData = repository.get(query, Operation.CacheElseSyncMainOperation).blockingFirst()

        assertEquals(expectedData, actualData)
        verify(exactly = 1) { cacheDatasource.get(query) }
        verify(exactly = 0) { cacheDatasource.put(any(), any()) }
        verify(exactly = 0) { mainDatasource.get(query) }
    }

    @Test
    fun `assert data fetched from main when cache-else-main-sync is requested but cache data source fails`() {
        val cacheDataInitial = Either.Left(DataException.DataNotFoundException())
        val mainData = Either.Right(anyList { anyTrending() })
        val cacheDataAfterCache = Either.Right(anyList { anyTrending() })
        val expectedData =
            listOf(
                Either.Right(anyList { anyTrending() }),
                Either.Left(DataException.DataNotFoundException())
            ).random()
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        every { cacheDatasource.get(query) } returns Flowable.just(cacheDataInitial) andThen Flowable.just(expectedData)
        every { mainDatasource.get(query) } returns Flowable.just(mainData)
        every { cacheDatasource.put(query, mainData.value) } returns Flowable.just(cacheDataAfterCache)

        val actualData  = repository.get(query, Operation.CacheElseSyncMainOperation).blockingFirst()

        assertEquals(expectedData, actualData)
        verify(exactly = 2) { cacheDatasource.get(query) }
        verify(exactly = 1) { mainDatasource.get(query) }
        verify(exactly = 1) { cacheDatasource.put(query, mainData.value) }
    }

    @Test
    fun `assert error is returned when both data sources fail when main-sync is requested`() {
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        val expectedData = Either.Left(DataException.DataNotFoundException())
        every { cacheDatasource.get(query) } returns Flowable.just(expectedData)
        every { mainDatasource.get(query) } returns Flowable.just(Either.Left(DataException.DataNotFoundException()))

        val actualData  = repository.get(query, Operation.SyncMainOperation).blockingFirst()

        assertEquals(expectedData, actualData)
        verify(exactly = 1) { cacheDatasource.get(query) }
        verify(exactly = 0) { cacheDatasource.put(any(), any()) }
        verify(exactly = 1) { mainDatasource.get(query) }
    }

    @Test
    fun `assert error is returned when both data sources fail when cache-else-main-sync is requested`() {
        val repository = givenRepository()
        val query = GetAllTrendingQuery()
        val expectedData = Either.Left(DataException.DataNotFoundException())
        every { cacheDatasource.get(query) } returns Flowable.just(expectedData)
        every { mainDatasource.get(query) } returns Flowable.just(Either.Left(DataException.DataNotFoundException()))

        val actualData  = repository.get(query, Operation.CacheElseSyncMainOperation).blockingFirst()

        assertEquals(expectedData, actualData)
        verify(exactly = 2) { cacheDatasource.get(query) }
        verify(exactly = 0) { cacheDatasource.put(any(), any()) }
        verify(exactly = 1) { mainDatasource.get(query) }
    }

    private fun givenRepository() = CacheRepository(
        Schedulers.trampoline(),
        cacheDatasource,
        mainDatasource
    )
}
