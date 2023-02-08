package com.daniel.fireauth.domain.di

import com.daniel.fireauth.data.repository.FireAuthRepositoryImpl
import com.daniel.fireauth.domain.repository.FireAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun providerFireAuthRepository(fireAuthRepositoryImpl: FireAuthRepositoryImpl): FireAuthRepository

}