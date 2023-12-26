package com.javdc.tussapp.data.remote.di

import android.content.SharedPreferences
import com.javdc.tussapp.data.BuildConfig
import com.javdc.tussapp.data.remote.api.AuthService
import com.javdc.tussapp.data.remote.api.CardService
import com.javdc.tussapp.data.remote.api.DateService
import com.javdc.tussapp.data.remote.api.NoticeService
import com.javdc.tussapp.data.remote.api.StopService
import com.javdc.tussapp.data.remote.api.interceptor.OAuthInterceptor
import com.javdc.tussapp.data.remote.datasource.CardRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.CardRemoteDataSourceImpl
import com.javdc.tussapp.data.remote.datasource.DateRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.DateRemoteDataSourceImpl
import com.javdc.tussapp.data.remote.datasource.NoticeRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.NoticeRemoteDataSourceImpl
import com.javdc.tussapp.data.remote.datasource.StopRemoteDataSource
import com.javdc.tussapp.data.remote.datasource.StopRemoteDataSourceImpl
import com.javdc.tussapp.data.remote.datasource.TokenDataSource
import com.javdc.tussapp.data.remote.datasource.TokenDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingLevel = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        return HttpLoggingInterceptor().setLevel(loggingLevel)
    }

    @Provides
    fun provideOAuthInterceptor(tokenDataSource: TokenDataSource): OAuthInterceptor =
        OAuthInterceptor(tokenDataSource)

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, oAuthInterceptor: OAuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(oAuthInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
            .also {
                it.dispatcher.maxRequests = 8
                it.dispatcher.maxRequestsPerHost = 4
            }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.tussam.es/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("okHttpClientForOAuthTokenRetrieval")
    fun provideOkHttpClientForOAuthTokenRetrieval(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @Named("retrofitForOAuthTokenRetrieval")
    fun provideRetrofitForOAuthTokenRetrieval(@Named("okHttpClientForOAuthTokenRetrieval") okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://auth.tussam.es/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthService(@Named("retrofitForOAuthTokenRetrieval") retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideCardService(retrofit: Retrofit): CardService =
        retrofit.create(CardService::class.java)

    @Provides
    @Singleton
    fun provideDateService(retrofit: Retrofit): DateService =
        retrofit.create(DateService::class.java)

    @Provides
    @Singleton
    fun provideNoticeService(retrofit: Retrofit): NoticeService =
        retrofit.create(NoticeService::class.java)

    @Provides
    @Singleton
    fun provideStopService(retrofit: Retrofit): StopService =
        retrofit.create(StopService::class.java)

    @Provides
    @Singleton
    fun provideTokenDataSource(
        sharedPreferences: SharedPreferences,
        authService: AuthService,
    ): TokenDataSource =
        TokenDataSourceImpl(sharedPreferences, authService)

    @Provides
    @Singleton
    fun provideCardRemoteDataSource(cardService: CardService): CardRemoteDataSource =
        CardRemoteDataSourceImpl(cardService)

    @Provides
    @Singleton
    fun provideStopDateRemoteDataSource(dateService: DateService): DateRemoteDataSource =
        DateRemoteDataSourceImpl(dateService)

    @Provides
    @Singleton
    fun provideNoticeRemoteDataSource(noticeService: NoticeService): NoticeRemoteDataSource =
        NoticeRemoteDataSourceImpl(noticeService)

    @Provides
    @Singleton
    fun provideStopRemoteDataSource(stopService: StopService): StopRemoteDataSource =
        StopRemoteDataSourceImpl(stopService)

}