package com.mecofarid.trending.ui.trending

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.mecofarid.shared.ui.trending.TrendingViewModel

@BindingAdapter("isLoading")
fun isLoading(view: View, state: TrendingViewModel.State) {
    view.isVisible = state == TrendingViewModel.State.Loading
}

@BindingAdapter("isNoData")
fun isNoData(view: View, state: TrendingViewModel.State) {
    view.isVisible = state == TrendingViewModel.State.NoData
}

@BindingAdapter("isSuccess")
fun isSuccess(view: View, state: TrendingViewModel.State) {
    view.isVisible = state is TrendingViewModel.State.Success
}
