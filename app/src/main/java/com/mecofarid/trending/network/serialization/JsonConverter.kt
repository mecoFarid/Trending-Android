package com.mecofarid.trending.network.serialization

import retrofit2.Converter

interface JsonConverter {
    fun converterFactory(): Converter.Factory
}
