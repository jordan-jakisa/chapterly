package com.keru.novelly.di

import com.keru.novelly.domain.use_cases.StreakUseCase
import com.keru.novelly.domain.use_cases.StreakUseCaseImpl
import com.keru.novelly.domain.use_cases.UsersInteractionsUseCase
import com.keru.novelly.domain.use_cases.UsersInteractionsUseCaseImpl
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
    abstract fun providesUserDetailsUseCase(userDetailsUseCaseImpl: UsersInteractionsUseCaseImpl): UsersInteractionsUseCase

    @Binds
    @Singleton
    abstract fun providesStreakUseCase(streakUseCaseImpl: StreakUseCaseImpl): StreakUseCase

}