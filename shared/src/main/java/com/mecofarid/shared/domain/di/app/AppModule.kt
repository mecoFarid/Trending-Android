package com.mecofarid.shared.domain.di.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Qualifier
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    @IoScheduler
    fun provideIoScheduler(): Scheduler = Schedulers.io()
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoScheduler