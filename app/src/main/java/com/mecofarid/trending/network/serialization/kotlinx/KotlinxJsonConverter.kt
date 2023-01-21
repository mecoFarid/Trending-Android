package com.mecofarid.trending.network.serialization.kotlinx

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mecofarid.trending.network.serialization.JsonConverter
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType
import retrofit2.Converter
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

private const val JSON_CONTENT_TYPE = "application/json; charset=utf-8"

class KotlinxJsonConverter(private val json: Json): JsonConverter {
    @ExperimentalSerializationApi
    override fun converterFactory(): Converter.Factory {
        return json.asConverterFactory(JSON_CONTENT_TYPE.toMediaType())
    }
}
