package com.javdc.tussapp.domain.usecase.di

import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.repository.DateRepository
import com.javdc.tussapp.domain.repository.FavoriteStopRepository
import com.javdc.tussapp.domain.repository.NoticeRepository
import com.javdc.tussapp.domain.repository.StopRepository
import com.javdc.tussapp.domain.usecase.AddStopToFavoritesUseCase
import com.javdc.tussapp.domain.usecase.AddStopToFavoritesUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetAvailableDatesUseCase
import com.javdc.tussapp.domain.usecase.GetAvailableDatesUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetCardAndSaveInLocalUseCase
import com.javdc.tussapp.domain.usecase.GetCardAndSaveInLocalUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetCardStatusUseCase
import com.javdc.tussapp.domain.usecase.GetCardStatusUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetCardsFromLocalUseCase
import com.javdc.tussapp.domain.usecase.GetCardsFromLocalUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetFavoriteStopsUseCase
import com.javdc.tussapp.domain.usecase.GetFavoriteStopsUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetLineDirectionStopsUseCase
import com.javdc.tussapp.domain.usecase.GetLineDirectionStopsUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetLinesUseCase
import com.javdc.tussapp.domain.usecase.GetLinesUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetNoticesUseCase
import com.javdc.tussapp.domain.usecase.GetNoticesUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetStopEstimatesUseCase
import com.javdc.tussapp.domain.usecase.GetStopEstimatesUseCaseImpl
import com.javdc.tussapp.domain.usecase.GetStopsWithLinesFromLocalUseCase
import com.javdc.tussapp.domain.usecase.GetStopsWithLinesFromLocalUseCaseImpl
import com.javdc.tussapp.domain.usecase.IsStopFavoriteUseCase
import com.javdc.tussapp.domain.usecase.IsStopFavoriteUseCaseImpl
import com.javdc.tussapp.domain.usecase.RemoveCardFromLocalUseCase
import com.javdc.tussapp.domain.usecase.RemoveCardFromLocalUseCaseImpl
import com.javdc.tussapp.domain.usecase.RemoveStopFromFavoritesUseCase
import com.javdc.tussapp.domain.usecase.RemoveStopFromFavoritesUseCaseImpl
import com.javdc.tussapp.domain.usecase.SyncAllUseCase
import com.javdc.tussapp.domain.usecase.SyncAllUseCaseImpl
import com.javdc.tussapp.domain.usecase.SyncCardsUseCase
import com.javdc.tussapp.domain.usecase.SyncCardsUseCaseImpl
import com.javdc.tussapp.domain.usecase.SyncDateVersionsUseCase
import com.javdc.tussapp.domain.usecase.SyncDateVersionsUseCaseImpl
import com.javdc.tussapp.domain.usecase.SyncStopsForTodayUseCase
import com.javdc.tussapp.domain.usecase.SyncStopsForTodayUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetStopEstimatesUseCase(stopRepository: StopRepository): GetStopEstimatesUseCase =
        GetStopEstimatesUseCaseImpl(stopRepository)

    @Provides
    fun provideGetNoticesUseCase(noticeRepository: NoticeRepository): GetNoticesUseCase =
        GetNoticesUseCaseImpl(noticeRepository)

    @Provides
    fun provideGetLinesUseCase(stopRepository: StopRepository): GetLinesUseCase =
        GetLinesUseCaseImpl(stopRepository)

    @Provides
    fun provideGetLineDirectionStopsUseCase(stopRepository: StopRepository): GetLineDirectionStopsUseCase =
        GetLineDirectionStopsUseCaseImpl(stopRepository)

    @Provides
    fun provideGetFavoriteStopsUseCase(favoriteStopRepository: FavoriteStopRepository): GetFavoriteStopsUseCase =
        GetFavoriteStopsUseCaseImpl(favoriteStopRepository)

    @Provides
    fun provideAddStopToFavoritesUseCase(favoriteStopRepository: FavoriteStopRepository): AddStopToFavoritesUseCase =
        AddStopToFavoritesUseCaseImpl(favoriteStopRepository)

    @Provides
    fun provideIsStopFavoriteUseCase(favoriteStopRepository: FavoriteStopRepository): IsStopFavoriteUseCase =
        IsStopFavoriteUseCaseImpl(favoriteStopRepository)

    @Provides
    fun provideRemoveStopFromFavoritesUseCase(favoriteStopRepository: FavoriteStopRepository): RemoveStopFromFavoritesUseCase =
        RemoveStopFromFavoritesUseCaseImpl(favoriteStopRepository)

    @Provides
    fun provideGetCardStatusUseCase(cardRepository: CardRepository): GetCardStatusUseCase =
        GetCardStatusUseCaseImpl(cardRepository)

    @Provides
    fun provideGetCardsFromLocalUseCase(cardRepository: CardRepository): GetCardsFromLocalUseCase =
        GetCardsFromLocalUseCaseImpl(cardRepository)

    @Provides
    fun provideGetCardAndSaveInLocalUseCase(
        cardRepository: CardRepository,
        getCardStatusUseCase: GetCardStatusUseCase,
    ): GetCardAndSaveInLocalUseCase =
        GetCardAndSaveInLocalUseCaseImpl(
            cardRepository,
            getCardStatusUseCase,
        )

    @Provides
    fun provideRemoveCardFromLocalUseCase(cardRepository: CardRepository): RemoveCardFromLocalUseCase =
        RemoveCardFromLocalUseCaseImpl(cardRepository)

    @Provides
    fun provideSyncAllUseCase(
        syncDateVersionsUseCase: SyncDateVersionsUseCase,
        syncStopsForTodayUseCase: SyncStopsForTodayUseCase,
        syncCardsUseCase: SyncCardsUseCase,
    ): SyncAllUseCase =
        SyncAllUseCaseImpl(
            syncDateVersionsUseCase,
            syncStopsForTodayUseCase,
            syncCardsUseCase,
        )

    @Provides
    fun provideSyncCardsUseCase(cardRepository: CardRepository): SyncCardsUseCase =
        SyncCardsUseCaseImpl(cardRepository)

    @Provides
    fun provideSyncStopsForTodayUseCase(
        stopRepository: StopRepository,
        dateRepository: DateRepository,
    ): SyncStopsForTodayUseCase =
        SyncStopsForTodayUseCaseImpl(
            stopRepository,
            dateRepository,
        )

    @Provides
    fun provideSyncDateVersionsUseCase(dateRepository: DateRepository): SyncDateVersionsUseCase =
        SyncDateVersionsUseCaseImpl(dateRepository)

    @Provides
    fun provideGetStopsWithLinesFromLocalUseCase(stopRepository: StopRepository): GetStopsWithLinesFromLocalUseCase =
        GetStopsWithLinesFromLocalUseCaseImpl(stopRepository)

    @Provides
    fun provideGetAvailableDatesUseCase(dateRepository: DateRepository): GetAvailableDatesUseCase =
        GetAvailableDatesUseCaseImpl(dateRepository)

}