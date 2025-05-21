package com.example.citieschallenge.di

import com.example.citieschallenge.data.local.IFavoritesDataStore
import com.example.citieschallenge.data.local.FavoritesDataStore
import com.example.citieschallenge.data.repository.ICityRepository
import com.example.citieschallenge.data.repository.CityRepository
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
        impl: CityRepository
    ): ICityRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesDataStore(
        impl: FavoritesDataStore
    ): IFavoritesDataStore
}
