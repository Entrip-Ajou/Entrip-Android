plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.paran.presentation"
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        testInstrumentationRunner = BuildConfig.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependencies.AndroidX.CORE)
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.AndroidX.CONSTRAINT_LAYOUT)
//    implementation(Dependencies.AndroidX.SWIPE_REFRESH_LAYOUT)
    implementation(Dependencies.AndroidX.LEGACY)
    implementation(Dependencies.AndroidX.ACTIVITY)
    implementation(Dependencies.AndroidX.NAV_FRAGMENT)
    implementation(Dependencies.AndroidX.NAV_UI)
    implementation(Dependencies.AndroidX.NAV_RUNTIME)
    implementation(Dependencies.AndroidX.LIFECYCLE_LIVEDATA)
    implementation(Dependencies.AndroidX.LIFECYCLE_VIEWMODEL)
    implementation(Dependencies.AndroidX.FRAGMENT)
    implementation(Dependencies.AndroidX.VIEW_PAGER_2)
    implementation(Dependencies.Google.MATERIAL)

    implementation(Dependencies.Coroutine.COROUTINE_ANDROID)
    implementation(Dependencies.Coroutine.COROUTINE_CORE)
    implementation(Dependencies.DotsIndicator.DOTS)

    implementation(Dependencies.Hilt.HILT)
    kapt(Dependencies.Hilt.HILT_COMPILER)

    implementation(Dependencies.Stomp.STOMP)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}