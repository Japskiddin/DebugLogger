plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("android.extensions")
}

apply(from = "./publish.gradle")

android {
  namespace = "io.github.japskiddin.debuglogger"

  packagingOptions {
    jniLibs {
      excludes += listOf("**/kotlin/**", "META-INF/androidx.*", "META-INF/proguard/androidx-*")
    }
    resources {
      excludes += listOf(
        "/META-INF/*.kotlin_module",
        "**/kotlin/**",
        "**/*.txt",
        "**/*.xml",
        "**/*.properties",
        "META-INF/DEPENDENCIES",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/license.txt",
        "META-INF/NOTICE",
        "META-INF/NOTICE.txt",
        "META-INF/notice.txt",
        "META-INF/ASL2.0",
        "META-INF/*.version",
        "META-INF/androidx.*",
        "META-INF/proguard/androidx-*"
      )
    }
  }

  compileSdk = AppConfig.compileSdk
  defaultConfig {
    minSdk = AppConfig.minSdk
    targetSdk = AppConfig.targetSdk
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
  implementation(AppDependencies.loggerLibraries)
}