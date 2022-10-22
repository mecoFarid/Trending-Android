package com.mecofarid.trending.common.network.moshi

import retrofit2.Converter

interface JsonConverter {
    fun converterFactory(): Converter.Factory
}