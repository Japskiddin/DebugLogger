import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.konan.properties.hasProperty
import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.library")
  kotlin("android")
  id("maven-publish")
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
  explicitApi = ExplicitApiMode.Strict
}

android {
  namespace = "io.github.japskiddin.debuglogger"
  compileSdk = libs.versions.androidSdk.compile.get().toInt()
  defaultConfig {
    minSdk = libs.versions.androidSdk.min.get().toInt()
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }
}

val libGroupId = "io.github.japskiddin"
val libArtifactId = "debuglogger"
val libVersion = "1.2.1"

val propertiesName = "github.properties"
val githubProperties = Properties().apply {
  if (rootProject.file(propertiesName).exists()) {
    load(FileInputStream(rootProject.file(propertiesName)))
  }
}
val outputsDirectoryPath = layout.buildDirectory.dir("outputs").get().toString()

publishing {
  publications {
    create<MavenPublication>("debugLogger") {
      groupId = libGroupId
      artifactId = libArtifactId
      version = libVersion
      artifact("${outputsDirectoryPath}/aar/${artifactId}-release.aar")
    }
  }

  repositories {
    maven {
      name = "GithubPackages"
      url = uri("https://maven.pkg.github.com/japskiddin/DebugLogger")
      credentials {
        username = if (githubProperties.hasProperty("gpr.usr")) {
          githubProperties.getProperty("gpr.usr")
        } else {
          System.getenv("GPR_USER")
        }
        password = if (githubProperties.hasProperty("gpr.key")) {
          githubProperties.getProperty("gpr.key")
        } else {
          System.getenv("GPR_API_KEY")
        }
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
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.recyclerView)
}
