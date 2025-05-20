package com.example.citieschallenge.di

import com.example.citieschallenge.data.local.FavoritesDataStore
import com.example.citieschallenge.data.local.FavoritesDataStoreImpl
import com.example.citieschallenge.data.repository.CityRepository
import com.example.citieschallenge.data.repository.CityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindCityRepository(
        impl: CityRepositoryImpl
    ): CityRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesDataStore(
        impl: FavoritesDataStoreImpl
    ): FavoritesDataStore
}
