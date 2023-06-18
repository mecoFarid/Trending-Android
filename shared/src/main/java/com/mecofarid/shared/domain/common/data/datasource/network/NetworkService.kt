package com.mecofarid.shared.domain.common.data.datasource.network

import com.mecofarid.shared.domain.common.data.Query
import io.reactivex.rxjava3.core.Single

interface NetworkService<T: Any> {
    fun get(query: Query): Single<T>
    fun put(query: Query, data: T): Single<T>
}
