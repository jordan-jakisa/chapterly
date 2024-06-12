package com.keru.novelly.domain.use_cases

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.keru.novelly.domain.repositories.UserRepository
import com.keru.novelly.utils.FirebasePaths
import com.keru.novelly.utils.Resource
import com.keru.novelly.utils.UNKNOWN_ERROR
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersInteractionsUseCaseImpl @Inject constructor(
    private val usersRepository: UserRepository,
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UsersInteractionsUseCase {
    override suspend fun hasUserLikedBook(bookId: String): Boolean {
        return when (val user = usersRepository.getUserDetails().first()) {
            is Resource.Success -> {
                val userData = user.data
                userData!!.likedBooks.contains(bookId)
            }

            is Resource.Error -> {
                Log.d("Novelly", "Error ==> ${user.message}")
                false
            }
        }
    }

    override suspend fun likeBook(bookId: String): Resource<Boolean> {
        return try {
            val userId = auth.currentUser?.uid
            val usersPath = FirebasePaths.Users.path
            val task = fireStore.collection(FirebasePaths.Users.path).document(userId!!)
                .update("likedBooks", FieldValue.arrayUnion(bookId)).await()
            Resource.Success(data = true)
        } catch (e: Exception) {
            Log.d("Novelly", "Error ==> ${e.message}")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR, data = false)
        }
    }

    override suspend fun disLikeBook(bookId: String): Resource<Boolean> {
        return try {
            val userId = auth.currentUser?.uid
            val usersPath = FirebasePaths.Users.path
            val task = fireStore.collection(FirebasePaths.Users.path).document(userId!!)
                .update("likedBooks", FieldValue.arrayRemove(bookId)).await()
            Resource.Success(data = true)
        } catch (e: Exception) {
            Log.d("Novelly", "Error ==> ${e.message}")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR, data = false)
        }
    }
}