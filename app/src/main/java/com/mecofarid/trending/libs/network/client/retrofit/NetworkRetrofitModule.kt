package com.mecofarid.trending.libs.network.client.retrofit

import com.mecofarid.trending.domain.common.data.DataException
import com.mecofarid.trending.domain.common.data.Mapper
import com.mecofarid.trending.domain.common.data.datasource.network.NetworkService
import com.mecofarid.trending.domain.di.network.NetworkComponent
import com.mecofarid.trending.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.trending.domain.features.trending.data.source.remote.service.TrendingService
import com.mecofarid.trending.libs.network.serialization.JsonConverter
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private const val BASE_URL = "https://api.github.com/"
private const val READ_WRITE_TIME_OUT = 30L
private const val CONNECT_TIME_OUT = 30L

class NetworkRetrofitModule(
    private val jsonConverter: JsonConverter
): NetworkComponent {

    private val retrofitService by lazy {
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

        retrofit.create(RetrofitService::class.java)
    }

    override fun trendingService(): NetworkService<List<TrendingRemoteEntity>> =
        TrendingService(retrofitService)
}
