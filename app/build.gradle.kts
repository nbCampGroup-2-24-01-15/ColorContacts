plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.colorcontacts"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.colorcontacts"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // viewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // viewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // motion
    implementation ("androidx.constraintlayout:constraintlayout:2.0.0-beta1")

    // colorPicker
    implementation ("com.github.Dhaval2404:ColorPicker:2.3")

    //progressbar
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")

    //RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // uri image
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // coil
    implementation("io.coil-kt:coil:1.4.0")

    // picasso
    implementation("com.squareup.picasso:picasso:2.8")

    // fast scroll bar
    implementation ("com.github.mond-al:recyclerview-fastscroller:1.0")

}