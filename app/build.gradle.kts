plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Enables ksp
    id("com.google.devtools.ksp")

    // Dagger hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.mardev.registroelettronico"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mardev.registroelettronico"
        minSdk = 26
        targetSdk = 34
        versionCode = 4
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //To use html Text
    implementation("com.github.ireward:compose-html:1.0.2")

    // To use collectAsStateWithLifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // To debug compose recomposition
    implementation("io.github.theapache64:rebugger:1.0.0-rc02")

    // To parse html received from the api
    implementation("org.jsoup:jsoup:1.17.1")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Compose navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Compose icons
    implementation("androidx.compose.material:material-icons-extended:1.6.4")

    // Kotlin coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Dagger hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    // Retrofit dependencies
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Material 3
    implementation ("androidx.compose.material:material")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Dagger hilt
// Allow references to generated code
kapt {
    correctErrorTypes = true
}