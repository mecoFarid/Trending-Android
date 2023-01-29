package com.mecofarid.trending.features.repo.data

import com.mecofarid.test.feature.repo.anyRepo
import com.mecofarid.test.feature.repo.anyRepoLocalEntity
import com.mecofarid.test.feature.repo.anyRepoRemoteEntity
import com.mecofarid.trending.anyList
import com.mecofarid.trending.domain.common.data.DataException
import com.mecofarid.trending.domain.common.data.Datasource
import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.common.data.Operation
import com.mecofarid.trending.domain.features.trending.data.TrendingRepository
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.trending.domain.features.trending.domain.model.Trending
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class TrendingRepositoryTest {


    @MockK
    private lateinit var cacheDatasource: Datasource<List<TrendingLocalEntity>>

    @MockK
    private lateinit var mainDatasource: Datasource<List<TrendingRemoteEntity>>

    @MockK
    private lateinit var toLocalEntityMapper: Mapper<TrendingRemoteEntity, TrendingLocalEntity>

    @MockK
    private lateinit var toDomainMapper: Mapper<TrendingLocalEntity, Trending>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main and cached when sync-main is requested`() = runTest {
        val expectedTrendingList = mutableListOf<Trending>()
        val trendingLocalEntityList = mutableListOf<TrendingLocalEntity>()
        val repoRemoteEntityList = anyList { anyRepoRemoteEntity() }
        val repository = givenRepository()
        val query = GetAllTrendingsQuery
        coEvery { mainDatasource.get(query) } returns repoRemoteEntityList
        repoRemoteEntityList.forEach {
            val localEntity = anyRepoLocalEntity()
            every { toLocalEntityMapper.map(it) } answers {
                trendingLocalEntityList.add(localEntity)
                localEntity
            }
            every { toDomainMapper.map(localEntity) } answers {
                val repo = anyRepo()
                expectedTrendingList.add(repo)
                repo
            }
        }
        coEvery { cacheDatasource.put(query, trendingLocalEntityList) } returns trendingLocalEntityList

        val actualRepoList  = repository.get(query, Operation.SyncMainOperation)

        assertEquals(expectedTrendingList, actualRepoList)
        repoRemoteEntityList.forEach {
            verify(exactly = 1) { toLocalEntityMapper.map(it) }
        }
        trendingLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.put(query, trendingLocalEntityList) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when sync-main is requested and main data source fails`() = runTest {
        val expectedTrendingList = mutableListOf<Trending>()
        val trendingLocalEntityList = mutableListOf<TrendingLocalEntity>()
        val repository = givenRepository()
        val query = GetAllTrendingsQuery
        coEvery { mainDatasource.get(query) } throws DataException.DataNotFoundException()
        coEvery { cacheDatasource.get(query) } returns trendingLocalEntityList

        val actualRepoList  = repository.get(query, Operation.SyncMainOperation)

        assertEquals(expectedTrendingList, actualRepoList)
        trendingLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when cache-else-main-sync is requested`() = runTest {
        val expectedTrendingList = mutableListOf<Trending>()
        val trendingLocalEntityList = mutableListOf<TrendingLocalEntity>()
        val repository = givenRepository()
        val query = GetAllTrendingsQuery
        coEvery { cacheDatasource.get(query) } returns trendingLocalEntityList

        val actualRepoList  = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(expectedTrendingList, actualRepoList)
        trendingLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify (exactly = 1) { cacheDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main when cache-else-main-sync is requested but cache data source fails`() = runTest {
        val expectedTrendingList = mutableListOf<Trending>()
        val trendingLocalEntityList = mutableListOf<TrendingLocalEntity>()
        val repoRemoteEntityList = anyList { anyRepoRemoteEntity() }
        val repository = givenRepository()
        val query = GetAllTrendingsQuery
        coEvery { cacheDatasource.get(query) } throws DataException.DataNotFoundException()
        coEvery { mainDatasource.get(query) } returns repoRemoteEntityList
        repoRemoteEntityList.forEach {
            val localEntity = anyRepoLocalEntity()
            every { toLocalEntityMapper.map(it) } answers {
                trendingLocalEntityList.add(localEntity)
                localEntity
            }
            every { toDomainMapper.map(localEntity) } answers {
                val repo = anyRepo()
                expectedTrendingList.add(repo)
                repo
            }
        }
        coEvery { cacheDatasource.put(query, trendingLocalEntityList) } returns trendingLocalEntityList

        val actualRepoList  = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(expectedTrendingList, actualRepoList)
        repoRemoteEntityList.forEach {
            verify(exactly = 1) { toLocalEntityMapper.map(it) }
        }
        trendingLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify (exactly = 1) { cacheDatasource.get(query) }
        coVerify (exactly = 1) { mainDatasource.get(query) }
        coVerify (exactly = 1) { cacheDatasource.put(query, trendingLocalEntityList) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert exception is thrown when both data sources fail when main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingsQuery
        coEvery { cacheDatasource.get(query) } throws DataException.DataNotFoundException()
        coEvery { mainDatasource.get(query) } throws DataException.DataNotFoundException()

        lateinit var exception: DataException
        try {
            repository.get(query, Operation.SyncMainOperation)
        }catch (e: DataException.DataNotFoundException){
            exception = e
        }

        assertTrue(exception is DataException.DataNotFoundException)
        coVerify (exactly = 1) { cacheDatasource.get(query) }
        coVerify (exactly = 1) { mainDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert exception is thrown when both data sources fail when cache-else-main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingsQuery
        coEvery { cacheDatasource.get(query) } throws DataException.DataNotFoundException()
        coEvery { mainDatasource.get(query) } throws DataException.DataNotFoundException()

        lateinit var exception: DataException
        try {
            repository.get(query, Operation.CacheElseSyncMainOperation)
        }catch (e: DataException.DataNotFoundException){
            exception = e
        }

        assertTrue(exception is DataException.DataNotFoundException)
        coVerify (exactly = 2) { cacheDatasource.get(query) }
        coVerify (exactly = 1) { mainDatasource.get(query) }
    }

    private fun givenRepository() = TrendingRepository(
        cacheDatasource,
        mainDatasource,
        toLocalEntityMapper,
        toDomainMapper
    )
}
