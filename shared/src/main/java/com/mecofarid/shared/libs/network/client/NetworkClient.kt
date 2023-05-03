package com.mecofarid.shared.libs.network.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query

class NetworkClient(private val apolloClient: ApolloClient){
    suspend fun <D : Query.Data> query(query: Query<D>) =
        apolloClient.query(query).execute().dataAssertNoErrors
}
