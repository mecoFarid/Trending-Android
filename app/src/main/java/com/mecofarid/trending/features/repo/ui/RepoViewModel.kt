package com.mecofarid.trending.features.repo.ui

import android.text.Editable.Factory
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.domain.interactor.GetRepoInteractor
import com.mecofarid.trending.features.repo.domain.model.Repo
import kotlinx.coroutines.*

class RepoViewModel(private val repoInteractor: GetRepoInteractor): ViewModel() {

    companion object {
        fun factory(repoInteractor: GetRepoInteractor) = viewModelFactory {
            initializer {
                RepoViewModel(repoInteractor)
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
                val data = repoInteractor(GetAllTrendingReposQuery, operation)
                State.Success(data)
            } catch (ignore: DataException.DataNotFoundException) {
                State.NoData
            }
        }
    }

    sealed class State {
        object Loading : State()
        object NoData : State()
        data class Success(val repos: List<Repo>) : State()
    }
}