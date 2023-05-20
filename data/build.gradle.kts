plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "ajou.paran.data"
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        testInstrumentationRunner = BuildConfig.testInstrumentationRunner
        consumerProguardFiles(BuildConfig.consumerProguardFiles)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(BuildConfig.getDefaultProguardFile),
                BuildConfig.proguardFiles
            )
        }
    }
    compileOptions {
        sourceCompatibility = BuildConfig.sourceCompatibility
        targetCompatibility = BuildConfig.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = BuildConfig.jvmTarget
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependencies.AndroidX.CORE)

    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.JUNIT_EXT)
    androidTestImplementation(Dependencies.Test.ESPRESSO)
    testImplementation(Dependencies.Test.COROUTINE)
    testImplementation(Dependencies.Test.TRUTH)
    testImplementation(Dependencies.Test.MOCKITO_WEB_SERVER)

    implementation(Dependencies.Hilt.HILT)
    kapt(Dependencies.Hilt.HILT_COMPILER)
    implementation(Dependencies.Retrofit.RETROFIT)
    implementation(Dependencies.Retrofit.RETROFIT_CONVERTER_GSON)
    implementation(Dependencies.OkHttp.OKHTTP)
    implementation(Dependencies.OkHttp.OKHTTP_LOGGING)
    implementation(Dependencies.Coroutine.COROUTINE_CORE)
    implementation(Dependencies.Coroutine.COROUTINE_ANDROID)
    implementation(Dependencies.AndroidX.ROOM)
    implementation(Dependencies.AndroidX.ROOM_RUNTIME)
    kapt(Dependencies.AndroidX.ROOM_COMPILER)
    implementation(Dependencies.DataStore.DATASTORE_PREFERENCE)
    implementation(Dependencies.DataStore.DATASTORE_PREFERENCE_CORE)
}