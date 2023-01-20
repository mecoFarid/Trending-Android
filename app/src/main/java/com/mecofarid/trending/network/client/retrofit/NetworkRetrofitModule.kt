package com.mecofarid.trending.network.client.retrofit

import com.mecofarid.trending.common.data.DataException
import com.mecofarid.trending.common.data.Mapper
import com.mecofarid.trending.di.network.NetworkComponent
import com.mecofarid.trending.network.serialization.JsonConverter
import com.mecofarid.trending.features.repo.data.source.remote.service.RepoRetrofitService
import com.mecofarid.trending.features.repo.data.source.remote.service.RepoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.github.com/"
private const val READ_WRITE_TIME_OUT = 30L
private const val CONNECT_TIME_OUT = 30L

class NetworkRetrofitModule(
    private val jsonConverter: JsonConverter
): NetworkComponent {

    private val service by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val exceptionMapper = object : Mapper<Throwable, Throwable> {
            override fun map(input: Throwable): Throwable = DataException.DataNotFoundException(input)
        }

        val client = OkHttpClient.Builder()
            .readTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RetrofitExceptionHandlerAdapterFactory(exceptionMapper))
            .addConverterFactory(jsonConverter.converterFactory())
            .build()

        retrofit.create(RepoRetrofitService::class.java)
    }

    override fun repoService(): RepoService = service
}