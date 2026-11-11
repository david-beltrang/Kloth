package com.example.kloth

import android.app.Application
import android.os.Build
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.functions.functions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        // Solo usar el emulador de Firebase si se ejecuta dentro del emulador de Android
        if (BuildConfig.DEBUG && BuildConfig.USE_FIREBASE_EMULATORS && isEmulator()) {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.functions.useEmulator("10.0.2.2", 5001)
        }
    }

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }
}
