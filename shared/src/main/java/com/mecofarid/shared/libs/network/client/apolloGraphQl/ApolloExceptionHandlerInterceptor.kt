package com.mecofarid.shared.libs.network.client.apolloGraphQl

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.mecofarid.shared.domain.common.data.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ApolloExceptionHandlerInterceptor @Inject constructor(): ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> =
        chain
            .proceed(request)
            .catch {
                throw when (it) {
                    is ApolloNetworkException -> NetworkException.ConnectionException(it)
                    is ApolloHttpException -> NetworkException.HttpException(it.statusCode)
                    else -> it
                }
            }
}
