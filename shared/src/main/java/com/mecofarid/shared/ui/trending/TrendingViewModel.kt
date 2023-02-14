package com.mecofarid.shared.ui.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.features.trending.data.query.GetAllTrendingQuery
import com.mecofarid.shared.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrendingViewModel @Inject constructor(private val trendingInteractor: GetTrendingInteractor): ViewModel() {

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
