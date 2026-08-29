package com.example.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.BuildConfig
import kotlinx.coroutines.tasks.await

class AuthService(private val context: Context) {
    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e("AuthService", "FirebaseAuth instance unavailable", e)
            null
        }
    }

    private val credentialManager: CredentialManager? by lazy {
        try {
            CredentialManager.create(context)
        } catch (e: Exception) {
            Log.e("AuthService", "CredentialManager instance unavailable", e)
            null
        }
    }

    val currentUser get() = auth?.currentUser

    suspend fun signInWithGoogle(): String? {
        val credManager = credentialManager ?: return null
        val serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        if (serverClientId.isBlank() || serverClientId == "YOUR_GOOGLE_WEB_CLIENT_ID_HERE") {
            Log.w("AuthService", "Google Web Client ID is not configured.")
            return null
        }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credManager.getCredential(
                request = request,
                context = context,
            )
            handleSignIn(result)
        } catch (e: Exception) {
            Log.e("AuthService", "Google Sign-In failed", e)
            null
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): String? {
        val firebaseAuth = auth ?: return null
        return try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = googleIdTokenCredential.idToken
            
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            authResult.user?.uid
        } catch (e: Exception) {
            Log.e("AuthService", "Token handling failed", e)
            null
        }
    }

    fun logout() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.e("AuthService", "Logout failed", e)
        }
    }
}
