package com.mecofarid.trending.common.ui.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mecofarid.trending.ui.trending.TrendingViewModel

@BindingAdapter("enablePullRefresh")
fun enablePullRefresh(view: SwipeRefreshLayout, state: TrendingViewModel.State) {
    view.isEnabled = state !is TrendingViewModel.State.Loading
    view.isRefreshing = false
}
