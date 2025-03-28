pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.2.0" apply false
        id("org.jetbrains.kotlin.android") version "1.9.22" apply false
        id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
        id("com.google.dagger.hilt.android") version "2.50" apply false
    }
}

// Cấu hình Java Home
// Commented out to use system default JDK
// gradle.beforeSettings {
//     System.setProperty("org.gradle.java.home", "C:\\Program Files\\OpenLogic\\jdk-17.0.14.7-hotspot")
// }

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "HospitalAppointmentSchedule"
include(":app")
