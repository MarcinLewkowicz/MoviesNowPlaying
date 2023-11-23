import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "pl.ml.demo.movies"
    compileSdk = 34

    defaultConfig {
        applicationId = "pl.ml.demo.movies"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "\"${getApiKey()}\"")
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.recyclerview)
    implementation(libs.coil.kt)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    kapt(libs.hilt.compiler)
    testImplementation(libs.androidx.test)
    testImplementation(libs.google.truth)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

fun getApiKey(): String {
    val propFile = rootProject.file("secret.properties")
    return if (propFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(propFile))
        properties.getProperty("apiKey")
    } else {
        System.getenv("MOVIES_NOW_PLAYING_API_KEY")
            ?: throw IllegalStateException("You must provide the ApiKey for the Movie DB API. You can do it by creating 'secret.properties' file containing 'apiKey' property in root dir of the project or by defining MOVIES_NOW_PLAYING_API_KEY environment variable.")
    }
}