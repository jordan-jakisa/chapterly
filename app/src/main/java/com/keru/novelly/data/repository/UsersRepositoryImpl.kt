package com.keru.novelly.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.keru.novelly.data.data_source.network.models.User
import com.keru.novelly.domain.repositories.UserRepository
import com.keru.novelly.utils.FirebasePaths
import com.keru.novelly.utils.Resource
import com.keru.novelly.utils.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    val currentUser = auth.currentUser

    override suspend fun getUserDetails(): Flow<Resource<User>> {
        val flow =
            MutableStateFlow<Resource<User>>(Resource.Error(message = UNKNOWN_ERROR))
        try {
            val path = FirebasePaths.Users.path
            val userId = auth.currentUser?.uid

            fireStore.collection(path).document(userId!!)
                .addSnapshotListener { value, error ->
                    if (value!!.exists()) {
                        val user = value.toObject(User::class.java)
                        flow.tryEmit(Resource.Success(data = user))
                    }
                    if (error != null) {
                        flow.tryEmit(
                            Resource.Error(
                                message = error.message ?: UNKNOWN_ERROR
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            flow.tryEmit(
                Resource.Error(message = e.message ?: UNKNOWN_ERROR)
            )
        }
        return flow
    }

    override suspend fun deleteUserDetails(): Resource<String> {
        return try {
            val currentUser = auth.currentUser
            fireStore.collection(FirebasePaths.Users.path).document(currentUser?.uid!!).delete()
                .addOnSuccessListener {
                    currentUser.delete()
                }.await()

            Resource.Success(data = "Deleted")
        } catch (e: Exception) {
            Resource.Success(data = e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun updateGenre(category: String) {
        val path = FirebasePaths.Users.path
        val ref = fireStore.collection(path).document(auth.currentUser?.uid ?: "")
        try {
            val snapshot = ref.get().await()
            if (snapshot.exists()) {
                val genres = snapshot.toObject(User::class.java)?.genresDownloaded ?: emptyList()
                val newGenres = genres.toMutableList().apply { add(category) }
                Log.d("genres", "$genres")
                Log.d("genres", "New g: $newGenres")
                ref.update("genresDownloaded", newGenres)
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("downloads", "Exception: $e")
        }
    }

    override suspend fun addBookToCompleted(name: String) {
        if (auth.currentUser != null) {
            fireStore.collection(FirebasePaths.Users.path).document(auth.currentUser!!.uid).update(
                "completedBooks", FieldValue.arrayUnion(name)
            ).addOnSuccessListener {
                Log.d("downloads", "$name saved to completed books!")
            }.addOnFailureListener {
                Log.d("downloads", "$name failed to save!")
            }
        }
    }

    override suspend fun uploadUserImage(uri: Uri): Resource<String> {
        Log.d("users", "Uploading user image")

        val path = "Users/${currentUser!!.uid}"
        return try {
            val ref = storage.getReference(path)
            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            Resource.Success(data = downloadUrl)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun updateUserDetails(name: String, image: String?): Resource<String> {
        Log.d("users", "Updating user information")
        return try {
            val path = FirebasePaths.Users.path + "/${currentUser!!.uid}"
            fireStore.document(path)
                .update("name", name)
                .await()
            if (image != null) fireStore.document(path).update("image", image).await()

            Log.d("users", "Updated")
            Resource.Success(data = "Updated!")
        } catch (e: Exception) {
            Log.d("users", "Error ==> ${e.message}")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }

    }
}