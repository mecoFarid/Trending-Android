package com.mecofarid.trending.common.ui.bindingadapter

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun isVisible(view: View, visible: Boolean){
    view.isVisible = visible
}
