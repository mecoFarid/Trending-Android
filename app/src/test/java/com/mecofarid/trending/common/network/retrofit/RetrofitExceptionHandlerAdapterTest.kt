package com.mecofarid.trending.common.network.retrofit

import com.mecofarid.trending.common.data.NetworkException
import com.mecofarid.trending.randomInt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

internal class RetrofitExceptionHandlerAdapterTest{
    companion object {
        lateinit var mockWebServer: MockWebServer
        lateinit var testService: TestService
        @BeforeClass
        @JvmStatic
        fun setUp() {
            mockWebServer = MockWebServer()
            mockWebServer.start()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(mockWebServer.url("/"))
                    .addCallAdapterFactory(RetrofitExceptionHandlerAdapterFactory())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            testService = retrofit.create(TestService::class.java)
        }

        @AfterClass
        @JvmStatic
        fun tearDown(){
            mockWebServer.shutdown()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `assert correct value is returned when response is success`()  = runTest{
        val name = "Name"
        enqueueWithBody(200, "{\"name\":\"$name\"}")
        assertEquals(name, testService.getUser().name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = NetworkException.HttpException::class)
    fun `assert exception is thrown when client side fails`() = runTest {
        val httpCode = randomInt(min = 300, max = 599)
        enqueueWithBody(httpCode, null)
        testService.getUser()
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
        suspend fun getUser(): User
    }

    data class User(val name: String)
}