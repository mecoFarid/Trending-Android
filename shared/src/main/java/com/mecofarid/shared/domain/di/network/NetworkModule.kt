package com.mecofarid.shared.domain.di.network

import android.util.Base64
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.mecofarid.shared.libs.network.client.NetworkClient
import com.mecofarid.shared.libs.network.client.apolloGraphQl.ApolloAuthorizationInterceptor
import com.mecofarid.shared.libs.network.client.apolloGraphQl.ApolloExceptionHandlerInterceptor
import com.mecofarid.shared.libs.network.client.apolloGraphQl.AuthHeader
import com.mecofarid.shared.libs.network.client.apolloGraphQl.X_AUTH_TOKEN
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private const val BASE_URL = "https://api.github.com/graphql"
private const val READ_WRITE_TIME_OUT = 30L
private const val CONNECT_TIME_OUT = 30L
private const val BASE64_ENCODED_X_AUTH_TOKEN = "Z2hwXzd5M0JaUmUzMmpscjBGV2NXZUJWVDZVZ0QxbFVOUzBUVzFYMg=="

@InstallIn(SingletonComponent::class)
@Module
interface NetworkModule {

    @InstallIn(SingletonComponent::class)
    @Module
    object NetworkModuleCompanion {
        @Singleton
        @Provides
        fun provideNetworkClient(
            authorizationInterceptor: HttpInterceptor,
            exceptionHandlerInterceptor: ApolloInterceptor,
        ): NetworkClient {
            val logging = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

            val client = OkHttpClient.Builder()
                .readTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(READ_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            val apolloClient = ApolloClient.Builder()
                .serverUrl(BASE_URL)
                .httpEngine(DefaultHttpEngine(client))
                .addHttpInterceptor(authorizationInterceptor)
                .addInterceptor(exceptionHandlerInterceptor)
                .build()

            return NetworkClient(apolloClient)
        }

        @Singleton
        @Provides
        @AuthHeader
        fun provideAuthHeader(): () -> @JvmSuppressWildcards Map<String, String> = {
            val bytes = Base64.decode(BASE64_ENCODED_X_AUTH_TOKEN, Base64.DEFAULT)
            val text = String(bytes, Charsets.UTF_8)
            mapOf(X_AUTH_TOKEN to text)
        }
    }

    @Binds
    fun bindAuthorizationInterceptor(
        apolloAuthorizationInterceptor: ApolloAuthorizationInterceptor
    ): HttpInterceptor

    @Binds
    fun bindExceptionHandlerInterceptor(
        apolloExceptionHandlerInterceptor: ApolloExceptionHandlerInterceptor
    ): ApolloInterceptor
}
