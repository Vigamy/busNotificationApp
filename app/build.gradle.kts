import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Adiciona plugin Hilt do catálogo
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
}

val localProperties = Properties().apply {
    // load only if the file exists so we don't throw when it's absent
    val lpFile = rootProject.file("local.properties")
    if (lpFile.exists()) {
        load(lpFile.inputStream())
    }
}

// Prefer local.properties -> environment variable -> empty string
val googleApiKey = localProperties.getProperty("GOOGLE_API_KEY")
    ?: System.getenv("GOOGLE_API_KEY")
    ?: ""

android {
    namespace = "com.will.busnotification"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.will.busnotification"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Use the resolved key; if missing this will be an empty string instead of the literal "null"
        buildConfigField("String", "GOOGLE_API_KEY", "\"$googleApiKey\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.media3.common.ktx)
    // removed hardcoded older navigation dependency; use version catalog
    // implementation("androidx.navigation:navigation-compose:2.7.7")
    //noinspection UseTomlInsteada
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose.android)

    // Hilt and related
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // --- Firebase (BoM + Firestore KTX) ---
    // Using the BoM ensures compatible versions for firebase libraries.
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// If you plan to use the full Firebase project configuration (recommended),
// place your `google-services.json` file under `app/` and apply the
// Google Services Gradle plugin. With the version catalog above you can
// add the plugin alias at the top like:
// plugins { alias(libs.plugins.google.services) }
// or apply it at the bottom with:
// apply(plugin = "com.google.gms.google-services")
// The google-services.json file is generated in the Firebase console (Project Settings).
