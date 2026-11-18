package com.example.kloth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.kloth.data.repository.AuthRepository
import com.example.kloth.data.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.example.kloth.ui.theme.KlothTheme
import dagger.hilt.android.AndroidEntryPoint

/** Punto de entrada principal de la aplicación**/
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var userRepository: UserRepository
    private var lastLanguageTagUpdate: Pair<String, String>? = null
    private var pendingLanguageTagUpdate: Pair<String, String>? = null
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        // La ubicacion se solicita al iniciar la app; el mapa la usa cuando se abre
        requestLocalNetworkPermissionIfNeeded()
    }

    private val localNetworkPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            android.util.Log.w(
                "LocalNetwork",
                "ACCESS_LOCAL_NETWORK was denied; debug Firebase emulators may be unreachable."
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KlothTheme {
                KlothApp()
            }
        }

        requestLocationPermissionIfNeeded()
    }

    override fun onResume() {
        super.onResume()
        val userId = authRepository.currentUser?.uid ?: return
        val languageTag = resources.configuration.locales[0].toLanguageTag()
        val updateKey = userId to languageTag
        if (updateKey == lastLanguageTagUpdate || updateKey == pendingLanguageTagUpdate) return

        pendingLanguageTagUpdate = updateKey
        lifecycleScope.launch {
            if (userRepository.updateLanguageTag(userId, languageTag).isSuccess) {
                lastLanguageTagUpdate = updateKey
            }
            if (pendingLanguageTagUpdate == updateKey) pendingLanguageTagUpdate = null
        }
    }

    private fun requestLocationPermissionIfNeeded() {
        val fineGranted = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        val coarseGranted = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            requestLocalNetworkPermissionIfNeeded()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun requestLocalNetworkPermissionIfNeeded() {
        // Android 17 (API 37) requires a runtime grant for local emulator traffic.
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 37 &&
            checkSelfPermission(Manifest.permission.ACCESS_LOCAL_NETWORK) != PackageManager.PERMISSION_GRANTED
        ) {
            localNetworkPermissionLauncher.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
        }
    }
}
