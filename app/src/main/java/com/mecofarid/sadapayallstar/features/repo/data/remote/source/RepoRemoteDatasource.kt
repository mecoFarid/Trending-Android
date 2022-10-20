package com.mecofarid.sadapayallstar.features.repo.data.remote.source

import com.mecofarid.sadapayallstar.common.Datasource
import com.mecofarid.sadapayallstar.common.Operation
import com.mecofarid.sadapayallstar.common.Query
import com.mecofarid.sadapayallstar.features.repo.data.remote.entity.RepoRemoteEntity

class RepoRemoteDatasource: Datasource<RepoRemoteEntity> {
    override suspend fun get(query: Query, operation: Operation): RepoRemoteEntity {
        TODO("Not yet implemented")
    }

    override suspend fun put(query: Query, t: RepoRemoteEntity): RepoRemoteEntity {
        TODO("Not yet implemented")
    }
}