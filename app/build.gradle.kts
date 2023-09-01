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
    setProperty("archivesBaseName", "debug_logger_sample_${versionCode}_${versionName}")
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  /**
   * To override source and target compatibility (if different from the
   * toolchain JDK version), add the following. All of these
   * default to the same value as kotlin.jvmToolchain. If you're using the
   * same version for these values and kotlin.jvmToolchain, you can
   * remove these blocks.
   */
  compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
      jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

dependencies {
  implementation(AppDependencies.sampleLibraries)
  implementation(project(":debuglogger"))
}