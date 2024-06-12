package com.keru.novelly.di

import android.content.Context
import com.keru.novelly.data.repository.BooksRepositoryImpl
import com.keru.novelly.domain.repositories.BooksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksModule {

    @Binds
    @Singleton
    abstract fun providesContext(@ApplicationContext context: Context): Context

    @Binds
    @Singleton
    abstract fun providesBooksRepository(booksRepositoryImpl: BooksRepositoryImpl): BooksRepository
}