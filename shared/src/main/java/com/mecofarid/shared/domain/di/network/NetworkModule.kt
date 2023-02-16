package com.mecofarid.shared.domain.di.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.features.trending.data.source.remote.service.TrendingService
import com.mecofarid.shared.libs.network.client.retrofit.RetrofitExceptionHandlerAdapterFactory
import com.mecofarid.shared.libs.network.client.retrofit.RetrofitService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://api.github.com/"
private const val READ_WRITE_TIME_OUT = 30L
private const val CONNECT_TIME_OUT = 30L

private const val JSON_CONTENT_TYPE = "application/json; charset=utf-8"

@ExperimentalSerializationApi
val networkModule = module {

    factory {
        val json = Json{
            ignoreUnknownKeys = true
        }

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .readTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RetrofitExceptionHandlerAdapterFactory())
            .addConverterFactory(json.asConverterFactory(JSON_CONTENT_TYPE.toMediaType()))
            .build()

        retrofit.create(RetrofitService::class.java)
    }

    factory {
        TrendingService(retrofitService = get())
    } bind NetworkService::class
}
