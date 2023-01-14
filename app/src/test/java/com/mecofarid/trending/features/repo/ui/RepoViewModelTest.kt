package com.mecofarid.trending.features.repo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mecofarid.test.feature.repo.anyRepo
import com.mecofarid.trending.helper.CoroutinesTestRule
import com.mecofarid.trending.anyList
import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.domain.interactor.GetRepoInteractor
import com.mecofarid.trending.helper.getOrAwaitValues
import com.mecofarid.trending.randomInt
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/*
* LiveData can not be observed properly. Reason: ViewModel starts updating LiveData as soon as it is
* initialized, only then we start observing all the LiveData values but latter skips first emission(s)
* */
//internal class RepoViewModelTest {
//
//    @get:Rule
//    val coroutinesTestRule = CoroutinesTestRule()
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @MockK
//    private lateinit var getRepoInteractor: GetRepoInteractor
//
//    @Before
//    fun setUp() {
//        MockKAnnotations.init(this)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `assert view state updated to loading and then to success when view loaded and data received`() =
//        runTest {
//            val repoList = anyList { anyRepo() }
//            val expectedStateList = listOf(RepoViewModel.State.Loading, RepoViewModel.State.Success(repoList))
//            val viewModel = givenViewModel{
//                coEvery {
//                    getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//                } returns repoList
//            }
//
//            val actualUiStateList = viewModel.uiState.getOrAwaitValues()
//
//            coVerify(exactly = 1) {
//                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//            }
//            assertEquals(expectedStateList, actualUiStateList)
//        }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `assert view state updated to loading and then to no_data when view loaded and data is no received`() =
//        runTest {
//            val expectedStateList = listOf(RepoViewModel.State.Loading, RepoViewModel.State.NoData)
//            val viewModel = givenViewModel{
//                coEvery {
//                    getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//                } throws DataException.DataNotFoundException()
//            }
//
//            val actualUiStateList = viewModel.uiState.getOrAwaitValues()
//
//            coVerify(exactly = 1) {
//                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//            }
//            assertEquals(expectedStateList, actualUiStateList)
//        }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `assert view state updated to loading and then to no_data when force refreshed and data is no received`() =
//        runTest {
//            val expectedStateList = listOf(
//                RepoViewModel.State.Loading,
//                RepoViewModel.State.NoData,
//                RepoViewModel.State.Loading,
//                RepoViewModel.State.NoData
//            )
//            val viewModel = givenViewModel{
//                coEvery {
//                    getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//                } throws DataException.DataNotFoundException()
//                coEvery {
//                    getRepoInteractor(GetAllTrendingReposQuery, Operation.SyncMainOperation)
//                } throws DataException.DataNotFoundException()
//            }
//
//            viewModel.refresh()
//            val actualUiStateList = viewModel.uiState.getOrAwaitValues()
//
//            coVerify(exactly = 1) {
//                getRepoInteractor(GetAllTrendingReposQuery, Operation.SyncMainOperation)
//            }
//            // 1 when view loaded, 1 when force refreshed
//            assertEquals(expectedStateList, actualUiStateList)
//        }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `assert view state is not updated when state is loading`() = runTest {
//        val expectedState = listOf(RepoViewModel.State.Loading)
//        val repoList = anyList { anyRepo() }
//        val viewModel = givenViewModel{
//            coEvery {
//                getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//            } coAnswers {
//                // Delay so that force refresh can be called multiple times
//                delay(2_000)
//                repoList
//            }
//        }
//
//        repeat(randomInt(1, 10)) {
//            viewModel.refresh()
//        }
//        val actualUiStateList = viewModel.uiState.getOrAwaitValues()
//
//        coVerify(exactly = 1) {
//            getRepoInteractor(GetAllTrendingReposQuery, Operation.CacheElseSyncMainOperation)
//        }
//        assertEquals(expectedState, actualUiStateList)
//    }
//
//    private fun givenViewModel(block: () -> Unit): RepoViewModel {
//        block()
//        return RepoViewModel(getRepoInteractor)
//    }
//}