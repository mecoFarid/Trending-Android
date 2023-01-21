package com.mecofarid.trending.common.ui.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mecofarid.trending.features.repo.ui.RepoViewModel

@BindingAdapter("enablePullRefresh")
fun enablePullRefresh(view: SwipeRefreshLayout, state: RepoViewModel.State) {
    view.isEnabled = state !is RepoViewModel.State.Loading
    view.isRefreshing = false
}
