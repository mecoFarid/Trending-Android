package com.mecofarid.sadapayallstar.common

interface Mapper<I, O> {
    fun map(i: I): O
}