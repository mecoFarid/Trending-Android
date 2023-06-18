package com.mecofarid.shared.common.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mecofarid.shared.domain.common.data.NetworkException
import com.mecofarid.shared.domain.di.network.JSON_CONTENT_TYPE
import com.mecofarid.shared.libs.network.client.retrofit.RetrofitExceptionHandlerAdapterFactory
import com.mecofarid.test.randomInt
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.http.GET

internal class RetrofitExceptionHandlerAdapterTest{
    companion object {
        lateinit var mockWebServer: MockWebServer
        lateinit var testService: TestService
        private val json = Json{
            ignoreUnknownKeys = true
        }
        @OptIn(ExperimentalSerializationApi::class)
        @BeforeClass
        @JvmStatic
        fun setUp() {
            mockWebServer = MockWebServer()
            mockWebServer.start()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(mockWebServer.url("/"))
                    .addCallAdapterFactory(RetrofitExceptionHandlerAdapterFactory())
                    .addConverterFactory(json.asConverterFactory(JSON_CONTENT_TYPE.toMediaType()))
                    .build()
            testService = retrofit.create(TestService::class.java)
        }

        @AfterClass
        @JvmStatic
        fun tearDown(){
            mockWebServer.shutdown()
        }
    }

    @Test
    fun `assert correct value is returned when response is success`() {
        val name = "Name"
        enqueueWithBody(200, "{\"name\":\"$name\"}")
        val subscriber = TestObserver<User>()
        testService.getUser().subscribe(subscriber)

        subscriber.await()
        subscriber.assertValue {
             name == it.name
        }
    }

    @Test
    fun `assert exception is returned when client side fails`() {
        val httpCode = randomInt(min = 300, max = 599)
        val expectedException = NetworkException.HttpException(httpCode)
        enqueueWithBody(httpCode, null)
        val subscriber = TestObserver<User>()
        testService.getUser().subscribe(subscriber)

        subscriber.await()
        subscriber.assertError {
            expectedException == it
        }
    }

    private fun enqueueWithBody(httpCode: Int, body: String?) {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(httpCode).apply {
                body?.let { setBody(it) }
            }
        )
    }

    interface TestService{
        @GET("/")
        fun getUser(): Single<User>
    }

    @Serializable
    data class User(val name: String)
}
