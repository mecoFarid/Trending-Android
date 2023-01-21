package com.mecofarid.trending.di

import android.app.Application
import androidx.room.Room
import com.mecofarid.trending.db.room.DbRoomModule
import com.mecofarid.trending.network.serialization.kotlinx.KotlinxJsonConverter
import com.mecofarid.trending.network.client.retrofit.NetworkRetrofitModule
import com.mecofarid.trending.features.repo.RepoComponent
import com.mecofarid.trending.features.repo.RepoModule
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

private const val DB_NAME = "repo.db"

interface AppComponent{
    fun repoComponent(): RepoComponent
}


class AppModule(application: Application): AppComponent {

    private val dbComponent by lazy {
        Room.databaseBuilder(
            application,
            DbRoomModule::class.java,
            DB_NAME
        ).build()
    }
    private val networkComponent by lazy {
        val json = Json{
            ignoreUnknownKeys = true
        }
        NetworkRetrofitModule(KotlinxJsonConverter(json))
    }

    private val module by lazy {
        RepoModule(dbComponent, networkComponent)
    }
    override fun repoComponent(): RepoComponent = module
}
