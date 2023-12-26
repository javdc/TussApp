package com.javdc.tussapp.data.di

import com.javdc.tussapp.data.local.datasource.CardLocalDataSource
import com.javdc.tussapp.data.local.datasource.DateLocalDataSource
import com.javdc.tussapp.data.local.datasource.FavoriteStopLocalDataSource
import com.javdc.tussapp.data.local.datasource.StopLocalDataSource
import com.javdc.tussapp.data.remote.datasource.CardRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.DateRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.NoticeRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.StopRemoteDataSource
import com.javdc.tussapp.data.repository.CardRepositoryImpl
import com.javdc.tussapp.data.repository.DateRepositoryImpl
import com.javdc.tussapp.data.repository.FavoriteStopRepositoryImpl
import com.javdc.tussapp.data.repository.NoticeRepositoryImpl
import com.javdc.tussapp.data.repository.StopRepositoryImpl
import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.repository.DateRepository
import com.javdc.tussapp.domain.repository.FavoriteStopRepository
import com.javdc.tussapp.domain.repository.NoticeRepository
import com.javdc.tussapp.domain.repository.StopRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCardRepository(
        cardRemoteDataSource: CardRemoteDataSource,
        cardLocalDataSource: CardLocalDataSource,
    ): CardRepository {
        return CardRepositoryImpl(
            cardRemoteDataSource,
            cardLocalDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideDateRepository(
        dateRemoteDataSource: DateRemoteDataSource,
        dateLocalDataSource: DateLocalDataSource,
    ): DateRepository {
        return DateRepositoryImpl(
            dateRemoteDataSource,
            dateLocalDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideFavoriteStopRepository(
        favoriteStopLocalDataSource: FavoriteStopLocalDataSource,
    ): FavoriteStopRepository {
        return FavoriteStopRepositoryImpl(
            favoriteStopLocalDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideNoticeRepository(
        noticeRemoteDataSource: NoticeRemoteDataSource,
    ): NoticeRepository {
        return NoticeRepositoryImpl(
            noticeRemoteDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideStopRepository(
        stopRemoteDataSource: StopRemoteDataSource,
        stopLocalDataSource: StopLocalDataSource,
    ): StopRepository {
        return StopRepositoryImpl(
            stopRemoteDataSource,
            stopLocalDataSource,
        )
    }

}