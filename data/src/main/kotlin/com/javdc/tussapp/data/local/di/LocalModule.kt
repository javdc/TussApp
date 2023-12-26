package com.javdc.tussapp.data.local.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.javdc.tussapp.data.local.dao.CardDao
import com.javdc.tussapp.data.local.dao.DateVersionDao
import com.javdc.tussapp.data.local.dao.FavoriteStopDao
import com.javdc.tussapp.data.local.dao.StopDao
import com.javdc.tussapp.data.local.database.AppDatabase
import com.javdc.tussapp.data.local.datasource.CardLocalDataSource
import com.javdc.tussapp.data.local.datasource.CardLocalDataSourceImpl
import com.javdc.tussapp.data.local.datasource.DateLocalDataSource
import com.javdc.tussapp.data.local.datasource.DateLocalDataSourceImpl
import com.javdc.tussapp.data.local.datasource.FavoriteStopLocalDataSource
import com.javdc.tussapp.data.local.datasource.FavoriteStopLocalDataSourceImpl
import com.javdc.tussapp.data.local.datasource.StopLocalDataSource
import com.javdc.tussapp.data.local.datasource.StopLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "tussapp-database")
            .build()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideCardStatusDao(database: AppDatabase): CardDao =
        database.cardStatusDao()

    @Provides
    @Singleton
    fun provideDateVersionDao(database: AppDatabase): DateVersionDao =
        database.dateVersionDao()

    @Provides
    @Singleton
    fun provideFavoriteStopDao(database: AppDatabase): FavoriteStopDao =
        database.favoriteStopDao()

    @Provides
    @Singleton
    fun provideStopDao(database: AppDatabase): StopDao =
        database.stopDao()

    @Provides
    @Singleton
    fun provideCardStatusLocalDataSource(cardDao: CardDao): CardLocalDataSource =
        CardLocalDataSourceImpl(cardDao)

    @Provides
    @Singleton
    fun provideDateLocalDataSource(dateVersionDao: DateVersionDao): DateLocalDataSource =
        DateLocalDataSourceImpl(dateVersionDao)

    @Provides
    @Singleton
    fun provideFavoriteStopLocalDataSource(favoriteStopDao: FavoriteStopDao): FavoriteStopLocalDataSource =
        FavoriteStopLocalDataSourceImpl(favoriteStopDao)

    @Provides
    @Singleton
    fun provideStopLocalDataSource(stopDao: StopDao, sharedPreferences: SharedPreferences): StopLocalDataSource =
        StopLocalDataSourceImpl(stopDao, sharedPreferences)

}