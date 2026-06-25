plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.mixtotrackmobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mixtotrackmobile"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17 
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // ========== EXISTENTES ==========
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // ========== RETROFIT ==========
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // ========== COROUTINES ==========
    implementation(libs.kotlinx.coroutines.android)

    // ========== VIEWMODEL & LIVEDATA ==========
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ========== NUEVAS DEPENDENCIAS ==========

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Hilt (Inyección de dependencias)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // DataStore (Preferencias)
    implementation(libs.androidx.datastore.preferences)

    // Glide (Imágenes)
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // OkHttp (Logging para Retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // WorkManager (Tareas en background)
    implementation(libs.androidx.work.runtime.ktx)

    // Testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // Hilt Testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
}