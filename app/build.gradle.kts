buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.li.almacen"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.li.almacen"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Fragments and Acitivity
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.activity:activity:1.8.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // Refrash layout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Floating button
    implementation("com.nambimobile.widgets:expandable-fab:1.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
