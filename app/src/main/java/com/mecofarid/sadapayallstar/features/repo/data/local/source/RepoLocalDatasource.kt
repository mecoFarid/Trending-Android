package com.mecofarid.sadapayallstar.features.repo.data.local.source

import com.mecofarid.sadapayallstar.common.Datasource
import com.mecofarid.sadapayallstar.common.Operation
import com.mecofarid.sadapayallstar.common.Query
import com.mecofarid.sadapayallstar.features.repo.data.local.entity.RepoLocalEntity

class RepoLocalDatasource: Datasource<RepoLocalEntity> {
    override suspend fun get(query: Query, operation: Operation): RepoLocalEntity {
        TODO("Not yet implemented")
    }

    override suspend fun put(query: Query, t: RepoLocalEntity): RepoLocalEntity {
        TODO("Not yet implemented")
    }
}