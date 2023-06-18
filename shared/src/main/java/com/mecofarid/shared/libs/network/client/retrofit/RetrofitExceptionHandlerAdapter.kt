package com.mecofarid.shared.libs.network.client.retrofit

import com.mecofarid.shared.domain.common.data.NetworkException
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

class RetrofitExceptionHandlerAdapter<T>(
    private val responseType: Type,
    private val delegate: CallAdapter<T, *>,
) : CallAdapter<T, Any> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Any {
        val single = delegate.adapt(call) as Single<Any>
        return single.onErrorResumeNext {
            val exception =
                when (it) {
                    is IOException -> NetworkException.ConnectionException(it)
                    is HttpException -> NetworkException.HttpException(it.code())
                    else -> it
                }

            Single.error(exception)
        }
    }
}
