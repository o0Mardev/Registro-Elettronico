// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0" apply false
    // Ksp plugin
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"

    // Dagger hilt
    id("com.google.dagger.hilt.android") version "2.57" apply false
}