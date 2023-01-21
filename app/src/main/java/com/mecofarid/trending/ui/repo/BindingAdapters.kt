package com.mecofarid.trending.ui.repo

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

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
