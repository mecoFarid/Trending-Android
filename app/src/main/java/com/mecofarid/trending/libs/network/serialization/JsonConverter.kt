package com.mecofarid.trending.libs.network.serialization

import retrofit2.Converter

interface JsonConverter {
    fun converterFactory(): Converter.Factory
}
