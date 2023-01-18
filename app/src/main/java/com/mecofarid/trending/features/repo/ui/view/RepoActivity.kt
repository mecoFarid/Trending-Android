package com.mecofarid.trending.features.repo.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mecofarid.trending.R
import com.mecofarid.trending.appComponent
import com.mecofarid.trending.databinding.ActivityRepoBinding
import com.mecofarid.trending.features.repo.domain.model.Repo
import com.mecofarid.trending.features.repo.ui.RepoViewModel

class RepoActivity : AppCompatActivity(){

    private val binding: ActivityRepoBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_repo) }
    private val viewModel by viewModels<RepoViewModel> {
        RepoViewModel.factory(application.appComponent().repoComponent().getRepoInteractor())
    }
    private val repoAdapter by lazy { RepoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initSuccessAdapter()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) {
            when (it) {
                is RepoViewModel.State.Success -> showSuccess(it.repos)
                else -> {}
            }
        }
    }

    private fun initSuccessAdapter(){
        binding.successHolder.dataHolder.apply {
            adapter = repoAdapter
        }
    }

    private fun showSuccess(repos: List<Repo>){
        val repoView = repos.map { RepoView(it) }
        repoAdapter.submitList(repoView)
    }
}