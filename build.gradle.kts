// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false

    // Ksp plugin
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false

    // Dagger hilt
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}