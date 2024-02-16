package com.keru.novelly.di

import com.keru.novelly.data.repository.UsersRepositoryImpl
import com.keru.novelly.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsersModule {

    @Binds
    @Singleton
    abstract fun providesUsersRepository(usersRepositoryImpl: UsersRepositoryImpl): UserRepository
}