plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.kapt")

}

android {
    namespace = "com.example.personalfinancemobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.personalfinancemobile"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true // opsional
        buildConfig = true // opsional
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.cast.framework)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


// RecyclerView untuk list dinamis
    implementation("androidx.recyclerview:recyclerview:1.3.1")

// Material Design Components (tombol, input, snackbar, dll)
    implementation("com.google.android.material:material:1.9.0")

// Retrofit untuk koneksi HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

// GSON converter untuk parsing JSON dari Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Glide untuk menampilkan gambar dari URL
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")


// OkHttp sebagai library jaringan (biasa digunakan di dalam Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // MPAndroidChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // dependensi Material
    implementation ("com.google.android.material:material:1.10.0")



}