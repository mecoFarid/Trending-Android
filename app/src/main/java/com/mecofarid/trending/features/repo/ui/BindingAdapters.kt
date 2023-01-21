package com.mecofarid.trending.features.repo.ui

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mecofarid.trending.R
import com.mecofarid.trending.features.repo.ui.RepoViewModel

@BindingAdapter("isLoading")
fun isLoading(view: View, state: RepoViewModel.State) {
    view.isVisible = state == RepoViewModel.State.Loading
}

@BindingAdapter("isNoData")
fun isNoData(view: View, state: RepoViewModel.State) {
    view.isVisible = state == RepoViewModel.State.NoData
}

@BindingAdapter("isSuccess")
fun isSuccess(view: View, state: RepoViewModel.State) {
    view.isVisible = state is RepoViewModel.State.Success
}
