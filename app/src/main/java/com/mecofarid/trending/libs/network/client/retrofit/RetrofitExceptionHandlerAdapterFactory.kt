package com.mecofarid.trending.libs.network.client.retrofit

import com.mecofarid.trending.domain.common.data.Mapper
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

class RetrofitExceptionHandlerAdapterFactory(
    private val exceptionMapper : Mapper<Throwable, Throwable>
): CallAdapter.Factory() {
    override fun get(
            returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Validate Call
        val typeCall = getRawType(returnType)
        if (typeCall != Call::class.java)
            return null

        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        val responseTypeClass = getRawType(typeCall)
        return RetrofitExceptionHandlerAdapter(responseType, responseTypeClass, exceptionMapper)
    }
}
