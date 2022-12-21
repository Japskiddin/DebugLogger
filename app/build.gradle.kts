plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("android.extensions")
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

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  viewBinding {
    android.buildFeatures.viewBinding = true
  }
}

dependencies {
  implementation(AppDependencies.sampleLibraries)
  implementation(project(":debuglogger"))
}