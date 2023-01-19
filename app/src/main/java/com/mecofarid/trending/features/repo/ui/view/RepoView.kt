package com.mecofarid.trending.features.repo.ui.view

import androidx.databinding.ObservableBoolean
import com.mecofarid.trending.features.repo.domain.model.Repo

data class RepoView(val repo: Repo) {
    val isExpanded: ObservableBoolean = ObservableBoolean(false)

    fun toggleExpansion(){
        isExpanded.set(!isExpanded.get())
    }
}
