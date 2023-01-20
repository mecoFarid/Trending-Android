package com.mecofarid.trending.network.serialization.moshi

import com.mecofarid.trending.network.serialization.JsonConverter
import com.squareup.moshi.Moshi
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

class MoshiJsonConverter(
    private val moshi: Moshi
): JsonConverter {
    override fun converterFactory(): Converter.Factory = MoshiConverterFactory.create(moshi)
}