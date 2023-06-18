package com.mecofarid.shared.features.repo.data.source.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.mecofarid.shared.domain.features.trending.data.source.local.dao.TrendingLocalEntityDao
import com.mecofarid.shared.domain.features.trending.data.source.local.entity.TrendingLocalEntity
import com.mecofarid.shared.libs.db.room.TrendingDatabase
import com.mecofarid.test.anyList
import com.mecofarid.test.feature.repo.anyTrendingLocalEntity
import com.mecofarid.test.randomInt
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
internal class TrendingLocalEntityDaoTest {

    private lateinit var repoDao: TrendingLocalEntityDao
    private lateinit var db: TrendingDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TrendingDatabase::class.java).build()
        repoDao = db.trendingLocalEntityDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun assert_persisted_and_fetched_repos_are_the_same() {
        val expectedRepos = generateRepoList().sortedBy {
            it.id
        }
        // To verify that table keeps only the last inserted data, we execute multiple insertions
        repeat(randomInt(min = 2, max = 10)) {
            repoDao.deleteAllTrendingAndInsert(expectedRepos)
        }

        val actualRepos = repoDao.getAllTrendings().blockingFirst().sortedBy {
            it.id
        }

        assertEquals(expectedRepos, actualRepos)
    }

    private fun generateRepoList(): List<TrendingLocalEntity> {
        var id = 0L
        return anyList {
            id++
            anyTrendingLocalEntity().copy(id = id)
        }
    }
}