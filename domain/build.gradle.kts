plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "ajou.paran.domain"
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
    implementation(Dependencies.Hilt.HILT)
    kapt(Dependencies.Hilt.HILT_COMPILER)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}