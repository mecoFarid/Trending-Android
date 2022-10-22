package com.mecofarid.trending.features.repo.data

import com.mecofarid.trending.anyList
import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Datasource
import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.test.feature.repo.anyRepo
import com.mecofarid.test.feature.repo.anyRepoLocalEntity
import com.mecofarid.test.feature.repo.anyRepoRemoteEntity
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.features.repo.data.source.remote.entity.RepoRemoteEntity
import com.mecofarid.trending.features.repo.domain.model.Repo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class RepoRepositoryTest {


    @MockK
    private lateinit var cacheDatasource: Datasource<List<RepoLocalEntity>>

    @MockK
    private lateinit var mainDatasource: Datasource<List<RepoRemoteEntity>>

    @MockK
    private lateinit var toLocalEntityMapper: Mapper<RepoRemoteEntity, RepoLocalEntity>

    @MockK
    private lateinit var toDomainMapper: Mapper<RepoLocalEntity, Repo>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main and cached when sync-main is requested`() = runTest {
        val expectedRepoList = mutableListOf<Repo>()
        val repoLocalEntityList = mutableListOf<RepoLocalEntity>()
        val repoRemoteEntityList = anyList { anyRepoRemoteEntity() }
        val repository = givenRepository()
        val query = GetAllTrendingReposQuery
        coEvery { mainDatasource.get(query) } returns repoRemoteEntityList
        repoRemoteEntityList.forEach {
            val localEntity = anyRepoLocalEntity()
            every { toLocalEntityMapper.map(it) } answers {
                repoLocalEntityList.add(localEntity)
                localEntity
            }
            every { toDomainMapper.map(localEntity) } answers {
                val repo = anyRepo()
                expectedRepoList.add(repo)
                repo
            }
        }
        coEvery { cacheDatasource.put(query, repoLocalEntityList) } returns repoLocalEntityList

        val actualRepoList  = repository.get(query, Operation.SyncMainOperation)

        assertEquals(expectedRepoList, actualRepoList)
        repoRemoteEntityList.forEach {
            verify(exactly = 1) { toLocalEntityMapper.map(it) }
        }
        repoLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.put(query, repoLocalEntityList) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when sync-main is requested and main data source fails`() = runTest {
        val expectedRepoList = mutableListOf<Repo>()
        val repoLocalEntityList = mutableListOf<RepoLocalEntity>()
        val repository = givenRepository()
        val query = GetAllTrendingReposQuery
        coEvery { mainDatasource.get(query) } throws DataException.DataNotFoundException()
        coEvery { cacheDatasource.get(query) } returns repoLocalEntityList

        val actualRepoList  = repository.get(query, Operation.SyncMainOperation)

        assertEquals(expectedRepoList, actualRepoList)
        repoLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify(exactly = 1) { mainDatasource.get(query) }
        coVerify(exactly = 1) { cacheDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from cache when cache-else-main-sync is requested`() = runTest {
        val expectedRepoList = mutableListOf<Repo>()
        val repoLocalEntityList = mutableListOf<RepoLocalEntity>()
        val repository = givenRepository()
        val query = GetAllTrendingReposQuery
        coEvery { cacheDatasource.get(query) } returns repoLocalEntityList

        val actualRepoList  = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(expectedRepoList, actualRepoList)
        repoLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify (exactly = 1) { cacheDatasource.get(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert data fetched from main when cache-else-main-sync is requested but cache data source fails`() = runTest {
        val expectedRepoList = mutableListOf<Repo>()
        val repoLocalEntityList = mutableListOf<RepoLocalEntity>()
        val repoRemoteEntityList = anyList { anyRepoRemoteEntity() }
        val repository = givenRepository()
        val query = GetAllTrendingReposQuery
        coEvery { cacheDatasource.get(query) } throws DataException.DataNotFoundException()
        coEvery { mainDatasource.get(query) } returns repoRemoteEntityList
        repoRemoteEntityList.forEach {
            val localEntity = anyRepoLocalEntity()
            every { toLocalEntityMapper.map(it) } answers {
                repoLocalEntityList.add(localEntity)
                localEntity
            }
            every { toDomainMapper.map(localEntity) } answers {
                val repo = anyRepo()
                expectedRepoList.add(repo)
                repo
            }
        }
        coEvery { cacheDatasource.put(query, repoLocalEntityList) } returns repoLocalEntityList

        val actualRepoList  = repository.get(query, Operation.CacheElseSyncMainOperation)

        assertEquals(expectedRepoList, actualRepoList)
        repoRemoteEntityList.forEach {
            verify(exactly = 1) { toLocalEntityMapper.map(it) }
        }
        repoLocalEntityList.forEach {
            verify(exactly = 1) { toDomainMapper.map(it) }
        }
        coVerify (exactly = 1) { cacheDatasource.get(query) }
        coVerify (exactly = 1) { mainDatasource.get(query) }
        coVerify (exactly = 1) { cacheDatasource.put(query, repoLocalEntityList) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert exception is thrown when both data sources fail when main-sync is requested`() = runTest {
        val repository = givenRepository()
        val query = GetAllTrendingReposQuery
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
        val query = GetAllTrendingReposQuery
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

    private fun givenRepository() = RepoRepository(
        cacheDatasource,
        mainDatasource,
        toLocalEntityMapper,
        toDomainMapper
    )
}