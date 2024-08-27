package com.openplaytech.openplay.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.openplaytech.openplay.database.MovieDbDatabase
import com.openplaytech.openplay.model.mappers.HomeDataModelMapperImpl
import com.openplaytech.openplay.mvvm.interactor.home.HomeInteractorImpl
import com.openplaytech.openplay.network.client.MovieClient

@InstallIn(SingletonComponent::class)
@Module
object HomeModule {

    @Provides
    fun provideHomeInteractor(
        movieClient: MovieClient,
        movieDbDatabase: MovieDbDatabase,
        mapperImpl: HomeDataModelMapperImpl
    ): HomeInteractorImpl {
        return HomeInteractorImpl(movieClient, movieDbDatabase, mapperImpl)
    }
}
