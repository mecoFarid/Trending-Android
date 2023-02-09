package com.mecofarid.trending.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mecofarid.trending.R
import com.mecofarid.trending.common.ui.ext.replaceFragment
import com.mecofarid.trending.databinding.ActivityMainBinding
import com.mecofarid.trending.ui.trending.view.TrendingFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTrendingScreen()
    }

    private fun addTrendingScreen() = replaceFragment(binding.container.id, TrendingFragment())
}
