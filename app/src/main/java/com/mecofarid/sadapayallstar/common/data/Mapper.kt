package com.mecofarid.sadapayallstar.common.data

interface Mapper<I, O> {
    fun map(input: I): O
}