package com.mecofarid.trending

import android.app.Application
import androidx.room.Room
import com.mecofarid.trending.common.db.DbComponent
import com.mecofarid.trending.common.db.room.DbRoomModule
import com.mecofarid.trending.common.network.NetworkComponent
import com.mecofarid.trending.common.network.moshi.MoshiJsonConverter
import com.mecofarid.trending.common.network.retrofit.NetworkRetrofitModule
import com.mecofarid.trending.features.repo.RepoComponent
import com.mecofarid.trending.features.repo.RepoModule
import com.squareup.moshi.Moshi

private const val DB_NAME = "repo.db"

interface AppComponent{
    fun repoComponent(): RepoComponent
}

class AppModule(application: Application): AppComponent{

    private val dbComponent by lazy {
        Room.databaseBuilder(
            application,
            DbRoomModule::class.java,
            DB_NAME
        ).build()
    }
    private val networkComponent by lazy {
        val moshi = Moshi.Builder().build()
        NetworkRetrofitModule(MoshiJsonConverter(moshi))
    }

    private val appModule by lazy {
        RepoModule(dbComponent, networkComponent)
    }
    override fun repoComponent(): RepoComponent = appModule
}