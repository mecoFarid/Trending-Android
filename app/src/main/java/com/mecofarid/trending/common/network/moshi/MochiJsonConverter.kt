package com.mecofarid.trending.common.network.moshi

import com.squareup.moshi.Moshi
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

class MoshiJsonConverter(
    private val moshi: Moshi
): JsonConverter {
    override fun converterFactory(): Converter.Factory = MoshiConverterFactory.create(moshi)
}