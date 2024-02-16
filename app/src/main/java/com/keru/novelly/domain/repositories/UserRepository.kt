package com.keru.novelly.domain.repositories

import android.net.Uri
import com.keru.novelly.data.data_source.network.models.User
import com.keru.novelly.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserDetails(): Flow<Resource<User>>

    suspend fun deleteUserDetails(): Resource<String>

    suspend fun updateGenre(category: String)
    suspend fun addBookToCompleted(name: String)

    suspend fun uploadUserImage(uri: Uri): Resource<String>

    suspend fun updateUserDetails(name: String, image: String?): Resource<String>

}