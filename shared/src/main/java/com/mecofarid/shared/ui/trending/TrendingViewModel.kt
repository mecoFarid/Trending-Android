package com.mecofarid.shared.ui.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import kotlinx.coroutines.launch

class TrendingViewModel(private val trendingInteractor: GetTrendingInteractor): ViewModel() {

    companion object {
        fun factory(repoInteractor: GetTrendingInteractor) = viewModelFactory {
            initializer {
                TrendingViewModel(repoInteractor)
            }
        }
    }

    private val internalUiState = MutableLiveData<State>(State.Loading)
    val uiState = internalUiState
    private var state: State = State.Loading
        set(value) {
            field = value
            internalUiState.postValue(value)
        }

    init {
        loadData(Operation.CacheElseSyncMainOperation)
    }

    fun refresh() {
        if (state == State.Loading)
            return
        loadData(Operation.SyncMainOperation)
    }

    private fun loadData(operation: Operation) {
        state = State.Loading
        viewModelScope.launch {
            println("KOK $operation")
            state = trendingInteractor(GetAllTrendingQuery(), operation)
                .fold(
                    ifLeft = { State.NoData },
                    ifRight = { State.Success(it) }
                )

            println("KOK $state")
        }
    }

    sealed class State {
        object Loading : State()
        object NoData : State()
        data class Success(val trendingList: List<Trending>) : State()
    }
}
