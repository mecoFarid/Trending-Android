package com.mecofarid.trending.features.repo.ui

import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Operation
import com.mecofarid.trending.features.repo.data.query.GetAllTrendingReposQuery
import com.mecofarid.trending.features.repo.domain.interactor.GetRepoInteractor
import com.mecofarid.trending.features.repo.domain.model.Repo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RepoPresenter (private val repoInteractor: GetRepoInteractor): CoroutineScope {
    interface View {
        fun notifyState(state: State)
    }

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    private var view: View? = null
    private var state: State = State.Loading
        set(value) {
            field = value
            view?.notifyState(field)
        }

    fun onViewLoaded(view: View) {
        this.view = view
        loadData(Operation.CacheElseSyncMainOperation)
    }

    fun onViewDestroyed() {
        view = null
        cancel()
    }

    fun forceRefresh() {
        if (state == State.Loading)
            return
        loadData(Operation.SyncMainOperation)
    }

    private fun loadData(operation: Operation) {
        state = State.Loading
        println("KOROKR")
        launch {
            state = try {
                val data = repoInteractor(GetAllTrendingReposQuery, operation)
                State.Data(data)
            } catch (ignore: DataException.DataNotFoundException) {
                State.NoData
            }
        }
    }

    sealed class State {
        object Loading : State()
        object NoData : State()
        data class Data(val repos: List<Repo>) : State()
    }
}