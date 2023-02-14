package com.mecofarid.trending.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mecofarid.trending.common.ui.resources.TrendingTheme
import com.mecofarid.trending.ui.navigation.MainNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrendingTheme {
                MainNavigation()
            }
        }
    }
}
