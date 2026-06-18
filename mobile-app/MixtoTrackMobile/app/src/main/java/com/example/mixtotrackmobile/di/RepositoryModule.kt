package com.example.mixtotrackmobile.di

import com.example.mixtotrackmobile.data.repository.PerfilRepository
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
    fun providePerfilRepository(
        perfilRepository: PerfilRepository
    ): PerfilRepository = perfilRepository
}