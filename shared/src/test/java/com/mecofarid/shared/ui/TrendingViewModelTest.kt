package com.mecofarid.shared.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.either.Either
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.shared.helper.CoroutinesTestRule
import com.mecofarid.shared.helper.awaitValues
import com.mecofarid.shared.ui.trending.TrendingViewModel
import com.mecofarid.shared.ui.trending.TrendingViewModel.State
import com.mecofarid.test.feature.repo.anyTrending
import com.mecofarid.trending.anyList
import com.mecofarid.trending.randomInt
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class TrendingViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getTrendingInteractor: GetTrendingInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to success when view loaded and data received`() =
        runTest {
            val data = Either.Right(anyList { anyTrending() })
            val expectedStateList = listOf(State.Loading, State.Success(data.value))
            coEvery {
                getTrendingInteractor(any(), any())
            } coAnswers  {
                delay(100)
                data
            }
            val viewModel = givenViewModel()

            val actualUiStateList = viewModel.uiState.awaitValues()

            coVerify(exactly = 1) {
                getTrendingInteractor(GetAllTrendingQuery(), Operation.CacheElseSyncMainOperation)
            }
            assertEquals(expectedStateList, actualUiStateList)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to no_data when view loaded and data is no received`() =
        runTest {
            val expectedStateList = listOf(State.Loading, State.NoData)
            coEvery {
                getTrendingInteractor(any(), any())
            } coAnswers {
                delay(100)
                Either.Left(com.mecofarid.shared.domain.common.data.DataException.DataNotFoundException())
            }
            val viewModel = givenViewModel()

            val actualUiStateList = viewModel.uiState.awaitValues()

            coVerify(exactly = 1) {
                getTrendingInteractor(GetAllTrendingQuery(), Operation.CacheElseSyncMainOperation)
            }
            assertEquals(expectedStateList, actualUiStateList)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to no_data when force refreshed and data is not received`() =
        runTest {
            val dataDelay = 50L
            val expectedStateList = listOf(
                State.Loading,
                State.NoData,
                State.Loading,
                State.NoData
            )
            coEvery {
                getTrendingInteractor(any(), any())
            } coAnswers {
                delay(dataDelay)
                Either.Left(com.mecofarid.shared.domain.common.data.DataException.DataNotFoundException())
            }
            val viewModel = givenViewModel()

            launch {
                // Small delay, so the UI state would have started being observed before we start refreshing
                delay(2 * dataDelay)
                viewModel.refresh()
            }
            val actualUiStateList = viewModel.uiState.awaitValues()

            coVerify(exactly = 1) {
                getTrendingInteractor(GetAllTrendingQuery(), Operation.SyncMainOperation)
            }
            assertEquals(expectedStateList, actualUiStateList)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state is not updated when state is loading`() = runTest {
        val dataDelay = 100L
        val data = Either.Right(anyList { anyTrending() })
        val expectedState = listOf(State.Loading, State.Success(data.value))
        coEvery {
            getTrendingInteractor(any(), any())
        } coAnswers {
            delay(dataDelay)
            data
        }
        val viewModel = givenViewModel()

        launch {
            // Small delay, so the UI state would have started being observed before we start refreshing
            delay(1/2 * dataDelay)
            repeat(randomInt(1, 10)) {
                viewModel.refresh()
            }
        }
        val actualUiStateList = viewModel.uiState.awaitValues()

        coVerify(exactly = 1) {
            getTrendingInteractor(GetAllTrendingQuery(), Operation.CacheElseSyncMainOperation)
        }
        assertEquals(expectedState, actualUiStateList)
    }

    private fun givenViewModel(): TrendingViewModel {
        return TrendingViewModel(getTrendingInteractor)
    }
}
