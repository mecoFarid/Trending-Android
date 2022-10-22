package com.mecofarid.trending.features.repo.data.source.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.mecofarid.test.feature.repo.anyRepoLocalEntity
import com.mecofarid.trending.anyList
import com.mecofarid.trending.common.db.room.DbRoomModule
import com.mecofarid.trending.features.repo.data.source.local.entity.RepoLocalEntity
import com.mecofarid.trending.randomInt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
internal class RepoLocalEntityDaoTest {

    private lateinit var repoDao: RepoLocalEntityDao
    private lateinit var db: DbRoomModule

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DbRoomModule::class.java).build()
        repoDao = db.repoLocalEntityDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun assert_persisted_and_fetched_repos_are_the_same() = runTest {
        val expectedRepos = generateRepoList().sortedBy {
            it.id
        }
        // To verify that table keeps only the last inserted data, we execute multiple insertions
        repeat(randomInt(min = 2, max = 10)) {
            repoDao.deleteAllTrendingReposAndInsert(expectedRepos)
        }

        val actualRepos = repoDao.getAllTrendingRepos().sortedBy {
            it.id
        }

        assertEquals(expectedRepos, actualRepos)
    }

    private fun generateRepoList(): List<RepoLocalEntity> {
        var id = 0
        return anyList {
            id++
            anyRepoLocalEntity().copy(id = id)
        }
    }
}