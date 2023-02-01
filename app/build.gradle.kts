plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "io.github.japskiddin.debuglogger.sample"

  compileSdk = AppConfig.compileSdk
  defaultConfig {
    applicationId = "io.github.japskiddin.debuglogger.sample"
    minSdk = AppConfig.minSdk
    targetSdk = AppConfig.targetSdk
    versionCode = AppConfig.versionCode
    versionName = AppConfig.versionName
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
}

dependencies {
  implementation(AppDependencies.sampleLibraries)
  implementation(project(":debuglogger"))
}