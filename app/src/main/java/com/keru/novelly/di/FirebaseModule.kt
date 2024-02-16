package com.keru.novelly.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.keru.novelly.data.data_source.local.DownloadedBooksLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore = Firebase.firestore


    @Provides
    @Singleton
    fun providesStorage(): FirebaseStorage = Firebase.storage

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesDownloadedBooksDataSource(@ApplicationContext context: Context) =
        DownloadedBooksLocalDataSource(context)
}