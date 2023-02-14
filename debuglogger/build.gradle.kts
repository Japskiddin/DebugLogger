import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.library")
  kotlin("android")
  id("maven-publish")
}

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

val propertiesName = "github.properties"
val githubProperties = Properties()
if (rootProject.file(propertiesName).exists()) {
  githubProperties.load(FileInputStream(rootProject.file(propertiesName)))
}

fun getVersionName(): String {
  return "1.1.2"
}

fun getArtificatId(): String {
  return "debuglogger"
}

publishing {
  publications {
    create<MavenPublication>("debugLogger") {
      groupId = "io.github.japskiddin"
      artifactId = getArtificatId()
      version = getVersionName()
      artifact("$buildDir/outputs/aar/debuglogger-release.aar")
    }
  }

  repositories {
    maven {
      name = "GithubPackages"
      url = uri("https://maven.pkg.github.com/japskiddin/DebugLogger")
      credentials {
        username = githubProperties.get("gpr.usr") as String ?: System.getenv("GPR_USER")
        password = githubProperties.get("gpr.key") as String ?: System.getenv("GPR_API_KEY")
      }
    }
  }
}

val sourceFiles = android.sourceSets.getByName("main").java.getSourceFiles()

tasks.register<Javadoc>("withJavadoc") {
  isFailOnError = false
  dependsOn(tasks.named("compileDebugSources"), tasks.named("compileReleaseSources"))

  // add Android runtime classpath
  android.bootClasspath.forEach { classpath += project.fileTree(it) }

  // add classpath for all dependencies
  android.libraryVariants.forEach { variant ->
    variant.javaCompileProvider.get().classpath.files.forEach { file ->
      classpath += project.fileTree(file)
    }
  }

  source = sourceFiles
}

tasks.register<Jar>("withJavadocJar") {
  archiveClassifier.set("javadoc")
  dependsOn(tasks.named("withJavadoc"))
  val destination = tasks.named<Javadoc>("withJavadoc").get().destinationDir
  from(destination)
}

tasks.register<Jar>("withSourcesJar") {
  archiveClassifier.set("sources")
  from(sourceFiles)
}

dependencies {
  implementation(AppDependencies.loggerLibraries)
}