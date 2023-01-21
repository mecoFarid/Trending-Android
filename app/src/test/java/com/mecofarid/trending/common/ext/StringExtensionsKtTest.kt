package com.mecofarid.trending.common.ext

import com.mecofarid.trending.domain.common.ext.toNA
import com.mecofarid.trending.randomString
import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtensionsKtTest{

    @Test
    fun `assert self is returned when string is not null`(){
        val expectedString = randomString()

        val actualString = expectedString.toNA()

        assertEquals(expectedString, actualString)
    }

    @Test
    fun `assert placeholder is returned when string is null`(){
        val string : String? = null

        val actualString = string.toNA()

        assertEquals("N/A", actualString)
    }
}
