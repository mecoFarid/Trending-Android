package com.mecofarid.trending.features.repo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mecofarid.trending.TrendingApplication
import com.mecofarid.trending.appComponent
import com.mecofarid.trending.databinding.ActivityRepoBinding

class RepoActivity : AppCompatActivity(), RepoPresenter.View {

    private val binding by lazy { ActivityRepoBinding.inflate(layoutInflater) }
    private val presenter by lazy { application.appComponent().repoComponent().repoPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        presenter.onViewLoaded(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun notifyState(state: RepoPresenter.State) {

    }
}