plugins {
    id("com.android.application")
    kotlin("android")
}

/**
 * Locate (and possibly download) a JDK used to build your kotlin
 * source code. This also acts as a default for sourceCompatibility,
 * targetCompatibility and jvmTarget. Note that this does not affect which JDK
 * is used to run the Gradle build itself, and does not need to take into
 * account the JDK version required by Gradle plugins (such as the
 * Android Gradle Plugin)
 */
kotlin {
    jvmToolchain(17)
}

android {
    namespace = "io.github.japskiddin.debuglogger.sample"
    buildToolsVersion = AppConfig.buildToolsVersion
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        applicationId = "io.github.japskiddin.debuglogger.sample"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        vectorDrawables {
            useSupportLibrary = true
        }
        setProperty("archivesBaseName", "debug_logger_sample_${versionCode}_${versionName}")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(AppDependencies.sampleLibraries)
    implementation(project(":debuglogger"))
}