package com.mecofarid.shared.libs.network.client.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RetrofitExceptionHandlerAdapterFactory: CallAdapter.Factory() {
    override fun get(
            returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Validate Call
        val typeCall = getRawType(returnType)
        if (typeCall != Call::class.java)
            return null

        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        val responseTypeClass = getRawType(typeCall)
        return RetrofitExceptionHandlerAdapter(responseType, responseTypeClass)
    }
}
