package com.mecofarid.trending.ui.trending.view

import androidx.databinding.ObservableBoolean
import com.mecofarid.shared.domain.features.trending.domain.model.Trending

data class TrendingView(val trending: Trending) {
    val isExpanded: ObservableBoolean = ObservableBoolean(false)

    fun toggleExpansion(){
        isExpanded.set(!isExpanded.get())
    }
}
