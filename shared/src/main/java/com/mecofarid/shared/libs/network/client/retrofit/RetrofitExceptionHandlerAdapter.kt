package com.mecofarid.shared.libs.network.client.retrofit

import com.mecofarid.shared.domain.common.data.NetworkException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

class RetrofitExceptionHandlerAdapter<T>(
    private val responseType: Type,
    private val responseTypeClass: Class<T>
) : CallAdapter<T, Call<T>> {

    override fun responseType(): Type = responseType

    private fun responseTypeClass(): Class<T> = responseTypeClass

    override fun adapt(call: Call<T>): BodyCall<T> = BodyCall(call, responseTypeClass())

    class BodyCall<T>(
        private val delegate: Call<T>,
        private val responseClass: Class<T>
    ) : Call<T> {
        override fun enqueue(callback: Callback<T>) {
            delegate.enqueue(
                object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        if (response.isSuccessful)
                            callback.onResponse(call, response)
                        else
                            callback.onFailure(
                                call,
                                NetworkException.HttpException(response.code())
                            )
                    }

                    override fun onFailure(call: Call<T>, t: Throwable) {
                        val exception =
                            if (t is IOException) NetworkException.ConnectionException(t)
                            else t
                        callback.onFailure(call, exception)
                    }
                })
        }

        override fun isExecuted() = delegate.isExecuted
        // Since suspend functions won't use this method, no need to implement
        override fun execute(): Response<T> = throw NotImplementedError("Method not implemented")
        override fun cancel() = delegate.cancel()
        override fun isCanceled() = delegate.isCanceled
        override fun clone(): Call<T>  = BodyCall(delegate.clone(), responseClass)
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
    }
}
