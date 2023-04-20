package com.mecofarid.shared.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.either.Either
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.shared.helper.CoroutinesTestRule
import com.mecofarid.shared.helper.awaitValues
import com.mecofarid.shared.ui.trending.TrendingViewModel
import com.mecofarid.shared.ui.trending.TrendingViewModel.State
import com.mecofarid.test.anyList
import com.mecofarid.test.feature.repo.anyTrending
import com.mecofarid.test.randomInt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class TrendingViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getTrendingInteractor: GetTrendingInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to success when view loaded and data received`() =
        runTest {
            val data = Either.Right(anyList { anyTrending() })
            val expectedStateList = listOf(State.Loading, State.Success(data.value))
            whenever(getTrendingInteractor(any(), any())).doSuspendableAnswer {
                delay(100)
                data
            }
            val viewModel = givenViewModel()

            val actualUiStateList = viewModel.uiState.awaitValues()

            verify(
                getTrendingInteractor,
                times(1)
            ).invoke(GetAllTrendingQuery(), Operation.CacheElseSyncMainOperation)
            assertEquals(expectedStateList, actualUiStateList)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state updated to loading and then to no_data when view loaded and data is no received`() =
        runTest {
            val expectedStateList = listOf(State.Loading, State.NoData)
            whenever(getTrendingInteractor(any(), any())).doSuspendableAnswer {
                delay(100)
                Either.Left(DataException.DataNotFoundException())
            }
            val viewModel = givenViewModel()

            val actualUiStateList = viewModel.uiState.awaitValues()

            verify(
                getTrendingInteractor,
                times(1)
            ).invoke(GetAllTrendingQuery(), Operation.CacheElseSyncMainOperation)
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
            whenever(getTrendingInteractor(any(), any())).doSuspendableAnswer {
                delay(dataDelay)
                Either.Left(DataException.DataNotFoundException())
            }
            val viewModel = givenViewModel()

            launch {
                // Small delay, so the UI state would have started being observed before we start refreshing
                delay(2 * dataDelay)
                viewModel.refresh()
            }
            val actualUiStateList = viewModel.uiState.awaitValues()

            verify(
                getTrendingInteractor,
                times(1)
            ).invoke(GetAllTrendingQuery(), Operation.SyncMainOperation)
            assertEquals(expectedStateList, actualUiStateList)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert view state is not updated when state is loading`() = runTest {
        val dataDelay = 100L
        val data = Either.Right(anyList { anyTrending() })
        val expectedState = listOf(State.Loading, State.Success(data.value))
        whenever(getTrendingInteractor(any(), any())).doSuspendableAnswer {
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

        verify(
            getTrendingInteractor,
            times(1)
        ).invoke(GetAllTrendingQuery(), Operation.CacheElseSyncMainOperation)
        assertEquals(expectedState, actualUiStateList)
    }

    private fun givenViewModel(): TrendingViewModel {
        return TrendingViewModel(getTrendingInteractor)
    }
}
