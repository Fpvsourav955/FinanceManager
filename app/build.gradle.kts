plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.sourav.financemanager"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.sourav.financemanager"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("com.github.Foysalofficial:NafisBottomNav:5.0")
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.google.android.gms:play-services-auth:21.5.1")
    implementation("com.google.android.gms:play-services-identity:18.1.0")
    implementation("com.github.bumptech.glide:glide:5.0.5")
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
    implementation("com.github.denzcoskun:ImageSlideShow:0.1.2")
    implementation("com.makeramen:roundedimageview:2.3.0")


    implementation ("androidx.room:room-runtime:2.8.4")
    annotationProcessor ("androidx.room:room-compiler:2.8.4")
    implementation ("com.google.firebase:firebase-auth:24.0.1")
    implementation ("com.github.bumptech.glide:glide:5.0.5")
    annotationProcessor ("com.github.bumptech.glide:compiler:5.0.5")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.10.0")
}