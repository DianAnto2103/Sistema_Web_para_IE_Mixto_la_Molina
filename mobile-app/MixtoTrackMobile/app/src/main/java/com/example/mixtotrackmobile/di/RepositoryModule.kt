package com.example.mixtotrackmobile.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // Los repositorios se inyectan automáticamente mediante @Inject constructor
    // Si necesitas vincular una interfaz a una implementación, usa @Binds aquí.
}