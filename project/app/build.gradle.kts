plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics) 
}

android {
    namespace = "com.example.kloth"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.kloth"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packaging{
        excludes += "META-INF/LICENSE.md"
        excludes += "META-INF/LICENSE-notice.md"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    // En unit tests (JVM) Android Log/APIs no existen; evita crash al llamar Log.e en el repo.
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

// MockK/Byte Buddy no soporta bien JDK 25 del JBR de Android Studio; las unitarias corren en 21.
tasks.withType<Test>().configureEach {
    javaLauncher.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    )
}

fun kaptAndroidTest(string: String) {}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation("androidx.compose.runtime:runtime-livedata:1.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.compose.navigation)
    ksp(libs.dagger.kapt)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    //firestore
    implementation("com.google.firebase:firebase-firestore")

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)

    //
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")

    //Adicionales
    testImplementation("io.mockk:mockk:1.13.14")

    //Courutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    //Alternativa a los asserts tradicionales
    testImplementation("com.google.truth:truth:1.4.2")
    androidTestImplementation("com.google.truth:truth:1.1.5")
    
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.52")
    
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    testImplementation(kotlin("test"))
    
    //e2e
    // AndroidX Test - Instrumented
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    // ActivityScenario
    androidTestImplementation ("androidx.test:core:1.5.0")
    
    // Para UI / instrumented tests (androidTest/)
        androidTestImplementation("io.mockk:mockk-android:1.13.14")
        
}