package com.mecofarid.sadapayallstar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mecofarid.sadapayallstar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}