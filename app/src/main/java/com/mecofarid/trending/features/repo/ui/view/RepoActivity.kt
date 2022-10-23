package com.mecofarid.trending.features.repo.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.mecofarid.trending.R
import com.mecofarid.trending.appComponent
import com.mecofarid.trending.databinding.ActivityRepoBinding
import com.mecofarid.trending.features.repo.domain.model.Repo
import com.mecofarid.trending.features.repo.ui.RepoPresenter


class RepoActivity : AppCompatActivity(), RepoPresenter.View {

    private val binding by lazy { ActivityRepoBinding.inflate(layoutInflater) }
    private val presenter by lazy { application.appComponent().repoComponent().repoPresenter() }
    private val repoAdapter by lazy { RepoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSuccessAdapter()
        initRefresh()
        presenter.onViewLoaded(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun notifyState(state: RepoPresenter.State) {
        when (state) {
            is RepoPresenter.State.Success -> showSuccess(state.repos)
            RepoPresenter.State.Loading -> showLoading()
            RepoPresenter.State.NoData -> showNoData()
        }
    }

    private fun initSuccessAdapter(){
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider_bg, theme)!!
        divider.setDrawable(drawable)
        binding.successHolder.dataHolder.apply {
            addItemDecoration(divider)
            adapter = repoAdapter
        }
    }

    private fun initRefresh() {
        binding.apply {
            noDataHolder.retryBtn.setOnClickListener {
                presenter.forceRefresh()
            }
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                presenter.forceRefresh()
            }
        }
    }

    private fun showNoData(){
        binding.apply {
            swipeRefresh.isEnabled = true
            noDataHolder.root.isVisible = true
            successHolder.root.isVisible = false
            loadingHolder.root.isVisible = false
        }
    }

    private fun showLoading(){
        binding.apply {
            swipeRefresh.isEnabled = false
            loadingHolder.root.isVisible = true
            noDataHolder.root.isVisible = false
            successHolder.root.isVisible = false
        }
    }

    private fun showSuccess(repos: List<Repo>){
        binding.apply {
            swipeRefresh.isEnabled = true
            successHolder.root.isVisible = true
            loadingHolder.root.isVisible = false
            noDataHolder.root.isVisible = false
        }

        val repoView = repos.map { RepoView(it) }
        repoAdapter.submitList(repoView)
    }
}