package com.example.kloth

import android.app.Application
import android.os.Build
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.functions.functions
import dagger.hilt.android.HiltAndroidApp
import org.maplibre.android.MapLibre

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        MapLibre.getInstance(this)

        // Solo conecta con los emuladores de Firebase en depuración sobre un emulador Android.
        if (BuildConfig.DEBUG && BuildConfig.USE_FIREBASE_EMULATORS && isEmulator()) {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.functions.useEmulator("10.0.2.2", 5001)
        }
    }

    private fun isEmulator(): Boolean =
        Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            Build.PRODUCT == "google_sdk"
}
