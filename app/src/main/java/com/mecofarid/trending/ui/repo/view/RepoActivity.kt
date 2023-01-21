package com.mecofarid.trending.ui.repo.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mecofarid.trending.R
import com.mecofarid.trending.app.appComponent
import com.mecofarid.trending.databinding.ActivityRepoBinding
import com.mecofarid.trending.domain.features.repo.domain.model.Repo
import com.mecofarid.trending.ui.repo.RepoViewModel

class RepoActivity : AppCompatActivity(){

    private val binding: ActivityRepoBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_repo) }
    private val viewModel by viewModels<RepoViewModel> {
        RepoViewModel.factory(application.appComponent().repoComponent().getRepoInteractor())
    }
    private val repoAdapter by lazy { RepoAdapter(this) }

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
