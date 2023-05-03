package com.mecofarid.shared.libs.network.client.apolloGraphQl

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import javax.inject.Inject
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthHeader

const val X_AUTH_TOKEN = "X_AUTH_TOKEN"

class ApolloAuthorizationInterceptor @Inject constructor(
    @AuthHeader private val headerProvider: () -> @JvmSuppressWildcards Map<String, String>
) : HttpInterceptor {
    override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
        val headers = headerProvider()
        val newRequest =
            request
                .newBuilder()
                .addHeader("Authorization", "Bearer ${headers[X_AUTH_TOKEN]}")
                .build()
        return chain.proceed(newRequest)
    }
}
