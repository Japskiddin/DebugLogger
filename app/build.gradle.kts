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
  compileSdk = libs.versions.androidSdk.compile.get().toInt()
  defaultConfig {
    applicationId = "io.github.japskiddin.debuglogger.sample"
    minSdk = libs.versions.androidSdk.min.get().toInt()
    targetSdk = libs.versions.androidSdk.target.get().toInt()
    versionCode = 1
    versionName = "1.0.1"
    vectorDrawables {
      useSupportLibrary = true
    }
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
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintLayout)
  implementation(libs.material)

  implementation(project(":debuglogger"))
}
