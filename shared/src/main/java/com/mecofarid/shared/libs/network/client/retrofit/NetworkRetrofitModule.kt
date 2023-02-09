package com.mecofarid.shared.libs.network.client.retrofit

import com.mecofarid.shared.domain.common.data.datasource.network.NetworkService
import com.mecofarid.shared.domain.di.network.NetworkComponent
import com.mecofarid.shared.domain.features.trending.data.source.remote.entity.TrendingRemoteEntity
import com.mecofarid.shared.domain.features.trending.data.source.remote.service.TrendingService
import com.mecofarid.shared.libs.network.serialization.JsonConverter
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

    private val retrofitService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val exceptionMapper = object :
            com.mecofarid.shared.domain.common.data.Mapper<Throwable, Throwable> {
            override fun map(input: Throwable): Throwable = com.mecofarid.shared.domain.common.data.DataException.DataNotFoundException(input)
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
