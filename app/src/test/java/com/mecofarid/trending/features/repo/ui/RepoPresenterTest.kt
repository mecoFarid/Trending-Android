package com.mecofarid.trending.features.repo.ui

import com.mecofarid.test.feature.repo.anyRepo
import com.mecofarid.trending.CoroutinesTestRule
import com.mecofarid.trending.anyList
import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.domain.interactor.GetRepoInteractor
import com.mecofarid.trending.randomInt
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class RepoPresenterTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @MockK
    private lateinit var getRepoInteractor: GetRepoInteractor

    @MockK
    private lateinit var view: RepoPresenter.View

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to success when view loaded and data received`() =
        runTest {
            val repoList = anyList { anyRepo() }
            val repoPresenter = givenPresenter()
            coEvery {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
            } returns repoList
            justRun { view.notifyState(any()) }

            repoPresenter.onViewLoaded(view)

            coVerify(exactly = 1) {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
            }
            coVerify(exactly = 1) { view.notifyState(RepoPresenter.State.Loading) }
            coVerify(exactly = 1) { view.notifyState(RepoPresenter.State.Success(repoList)) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to no_data when view loaded and data is no received`() =
        runTest {
            val repoPresenter = givenPresenter()
            coEvery {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
            } throws DataException.DataNotFoundException()
            justRun { view.notifyState(any()) }

            repoPresenter.onViewLoaded(view)

            coVerify(exactly = 1) {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
            }
            coVerify(exactly = 1) { view.notifyState(RepoPresenter.State.Loading) }
            coVerify(exactly = 1) { view.notifyState(RepoPresenter.State.NoData) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to no_data when force refreshed and data is no received`() =
        runTest {
            val repoPresenter = givenPresenter()
            coEvery {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
            } throws DataException.DataNotFoundException()
            coEvery {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.SyncMainOperation)
            } throws DataException.DataNotFoundException()
            justRun { view.notifyState(any()) }

            repoPresenter.onViewLoaded(view)
            repoPresenter.forceRefresh()

            coVerify(exactly = 1) {
                getRepoInteractor(GetAllTrendingReposQuery, Operation.SyncMainOperation)
            }
            // 1 when view loaded, 1 when force refreshed
            coVerify(exactly = 2) { view.notifyState(RepoPresenter.State.Loading) }
            coVerify(exactly = 2) { view.notifyState(RepoPresenter.State.NoData) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state is not updated when state is loading`() = runTest {
        val repoList = anyList { anyRepo() }
        val repoPresenter = givenPresenter()
        coEvery {
            getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
        } coAnswers {
            // Delay so that force refresh can be called multiple times
            delay(2_000)
            repoList
        }
        justRun { view.notifyState(any()) }

        repoPresenter.onViewLoaded(view)
        repeat(randomInt(1, 10)) {
            repoPresenter.forceRefresh()
        }

        coVerify(exactly = 1) {
            getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
        }
        coVerify(exactly = 1) { view.notifyState(RepoPresenter.State.Loading) }
    }


    private fun givenPresenter() = RepoPresenter(getRepoInteractor)
}