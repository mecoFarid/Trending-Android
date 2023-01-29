package com.mecofarid.trending.ui.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mecofarid.trending.domain.common.data.DataException
import com.mecofarid.trending.domain.common.data.Operation
import com.mecofarid.trending.domain.features.trending.data.query.GetAllTrendingsQuery
import com.mecofarid.trending.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.trending.domain.features.trending.domain.model.Trending
import kotlinx.coroutines.launch

class TrendingViewModel(private val repoInteractor: GetTrendingInteractor): ViewModel() {

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
            state = try {
                val data = repoInteractor(GetAllTrendingsQuery, operation)
                State.Success(data)
            } catch (ignore: DataException.DataNotFoundException) {
                State.NoData
            }
        }
    }

    sealed class State {
        object Loading : State()
        object NoData : State()
        data class Success(val trendingList: List<Trending>) : State()
    }
}
