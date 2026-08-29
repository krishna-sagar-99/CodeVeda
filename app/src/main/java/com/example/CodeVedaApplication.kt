package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class CodeVedaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val app = FirebaseApp.initializeApp(this)
                if (app == null) {
                    val apiKey = if (BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY") {
                        BuildConfig.GEMINI_API_KEY
                    } else {
                        "AIzaSyDummyKeyForAppInitialization12345"
                    }
                    val options = FirebaseOptions.Builder()
                        .setApplicationId("1:123456789012:android:codeveda")
                        .setProjectId("codeveda-app")
                        .setApiKey(apiKey)
                        .build()
                    FirebaseApp.initializeApp(this, options)
                }
            }
        } catch (e: Exception) {
            Log.e("CodeVedaApplication", "Firebase initialization caught safely", e)
        }
    }
}
