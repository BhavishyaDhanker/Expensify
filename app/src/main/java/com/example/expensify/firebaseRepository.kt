package com.example.expensify

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signUpAndCreateProfile(
        userModel: User,
        password: String,
    ): Result<Unit>{
        return try {
            val authResult =  auth.createUserWithEmailAndPassword(userModel.email, password).await()

            val uid = authResult.user?.uid ?: ""
            val finalUser = userModel.copy(uid = uid)

            db.collection("Users").document(uid).set(finalUser).await()

            Result.success(Unit)
        } catch (e : Exception){
            Result.failure(e)
        }
    }


    suspend fun logIn(email: String, pass: String): Result<User?> {
        return try{
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val uid = result?.user?.uid ?: throw Exception("User UID not found")

            val currentUser = db.collection("Users").document(uid)
                .get()
                .await()
                .toObject(User::class.java)
            Result.success(currentUser)
        }
        catch(e: Exception){
            Result.failure(e)
        }
   }
}

