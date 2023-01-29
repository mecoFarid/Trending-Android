package com.mecofarid.trending.app

import android.app.Application
import androidx.room.Room
import com.mecofarid.trending.domain.di.AppComponent
import com.mecofarid.trending.domain.features.trending.TrendingComponent
import com.mecofarid.trending.domain.features.trending.TrendingModule
import com.mecofarid.trending.libs.db.room.DbRoomModule
import com.mecofarid.trending.libs.network.client.retrofit.NetworkRetrofitModule
import com.mecofarid.trending.libs.network.serialization.kotlinx.KotlinxJsonConverter
import kotlinx.serialization.json.Json

private const val DB_NAME = "repo.db"

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
        TrendingModule(dbComponent, networkComponent)
    }
    override fun trendingComponent(): TrendingComponent = module
}
