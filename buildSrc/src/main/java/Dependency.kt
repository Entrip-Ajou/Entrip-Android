object Versions {
    const val NAV = "2.5.3"
    const val LIFECYCLE = "2.6.1"
    const val FRAGMENT = "1.5.6"
    const val ROOM = "2.5.1"
    const val HILT = "2.44"
    const val COROUTINE = "1.6.4"
    const val OKHTTP = "5.0.0-alpha.10"
    const val RETROFIT = "2.9.0"
    const val GLIDE = "4.14.2"
    const val COIL = "1.4.0"
    const val DATASTORE = "1.0.0"
}

sealed class Dependencies {

    object AndroidX : Dependencies() {
        const val CORE = "androidx.core:core-ktx:1.9.0"
        const val APPCOMPAT = "androidx.appcompat:appcompat:1.4.2"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val SWIPE_REFRESH_LAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val LEGACY = "androidx.legacy:legacy-support-v4:1.0.0"
        const val ACTIVITY = "androidx.activity:activity-ktx:1.7.0"
        const val NAV_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAV}"
        const val NAV_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAV}"
        const val NAV_RUNTIME = "androidx.navigation:navigation-runtime-ktx:${Versions.NAV}"
        const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
        const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
        const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
        const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
        const val ROOM = "androidx.room:room-ktx:${Versions.ROOM}"
        const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
        const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}" // kapt
        const val VIEW_PAGER_2 = "androidx.viewpager2:viewpager2:1.0.0"
    }

    object Test : Dependencies() {
        const val JUNIT = "junit:junit:4.13.2" // testImplementation
        const val JUNIT_EXT = "androidx.test.ext:junit:1.1.5" // androidTestImplementation
        const val ESPRESSO = "androidx.test.espresso:espresso-core:3.5.1" // androidTestImplementation
        const val ROBOLECTRIC = "org.robolectric:robolectric:4.9" // testImplementation
        const val MOCKITO = "org.mockito:mockito-core:1.10.19" // testImplementation
        const val MOCKITO_WEB_SERVER = "com.squareup.okhttp3:mockwebserver:4.10.0" // testImplementation
        const val HAMCREST_LIBRARY = "org.hamcrest:hamcrest-library:2.2" // testImplementation
        const val COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2" // testImplementation
        const val TRUTH = "com.google.truth:truth:1.1.3" // testImplementation
    }

    object Hilt : Dependencies() {
        const val HILT = "com.google.dagger:hilt-android:${Versions.HILT}"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}" // kapt
    }

    object Coroutine : Dependencies() {
        const val COROUTINE_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}"
        const val COROUTINE_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}"
    }

    object OkHttp : Dependencies() {
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
        const val OKHTTP_LOGGING = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    }

    object Retrofit : Dependencies() {
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
        const val RETROFIT_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
        const val RETROFIT_RXJAVA2 = "com.squareup.retrofit2:adapter-rxjava2:${Versions.RETROFIT}"
    }

    object Google : Dependencies()  {
        const val MATERIAL = "com.google.android.material:material:1.6.0"
        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:30.0.0"  // implementation platform
        const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx"
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
        const val FIREBASE_FCM = "com.google.firebase:firebase-messaging-ktx"
        const val GOOGLE_AUTH = "com.google.android.gms:play-services-auth:20.4.1"
    }

    object Glide : Dependencies()  {
        const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
        const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}" // annotationProcessor
    }

    object CircleImageView : Dependencies()  {
        const val CIRCLE_IMAGE_VIEW = "de.hdodenhof:circleimageview:3.1.0"
    }

    object RX : Dependencies()  {
        const val RXJAVA2 = "io.reactivex.rxjava2:rxjava:2.2.5"
        const val RXJAVA2_ANDROID = "io.reactivex.rxjava2:rxandroid:2.1.0"
    }

    object Stomp : Dependencies()  {
        const val STOMP = "com.github.NaikSoftware:StompProtocolAndroid:1.6.6"
    }

    object Coil : Dependencies() {
        const val COIL = "io.coil-kt:coil:${Versions.COIL}"
        const val COIL_VIDEO = "io.coil-kt:coil-video:${Versions.COIL}"
    }

    object JsonSerialization : Dependencies() {
        const val JSON_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2"
    }

    object DataStore : Dependencies() {
        const val DATASTORE = "androidx.datastore:datastore:${Versions.DATASTORE}"
        const val DATASTORE_PREFERENCE = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"
        const val DATASTORE_PREFERENCE_CORE = "androidx.datastore:datastore-preferences-core:${Versions.DATASTORE}"
    }

    object DotsIndicator : Dependencies() {
        const val DOTS = "com.tbuonomo:dotsindicator:4.2"
    }

}