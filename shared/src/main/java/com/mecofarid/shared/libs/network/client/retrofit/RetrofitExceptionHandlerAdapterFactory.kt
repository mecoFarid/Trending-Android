package com.mecofarid.shared.libs.network.client.retrofit

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RetrofitExceptionHandlerAdapterFactory: CallAdapter.Factory() {
    private val rxJava3CallAdapterFactory by lazy { RxJava3CallAdapterFactory.create() }
    override fun get(
            returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)

        val delegateAdapter = rxJava3CallAdapterFactory.get(returnType, annotations, retrofit)
        return delegateAdapter?.run {
            return RetrofitExceptionHandlerAdapter(responseType = responseType, delegate = this)
        }
    }
}
