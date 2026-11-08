package com.example.kloth.dataSourceTest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry

internal fun grantLocalNetworkPermissionForFirebaseEmulator() {
    if (Build.VERSION.SDK_INT < 37) return

    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val targetContext = instrumentation.targetContext
    val permission = Manifest.permission.ACCESS_LOCAL_NETWORK
    if (targetContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        instrumentation.uiAutomation.grantRuntimePermission(targetContext.packageName, permission)
    }
}
