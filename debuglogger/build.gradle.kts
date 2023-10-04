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
}

android {
    namespace = "io.github.japskiddin.debuglogger"
    buildToolsVersion = AppConfig.buildToolsVersion
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        minSdk = AppConfig.minSdk
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        jniLibs {
            excludes += listOf(
                "**/kotlin/**",
                "META-INF/androidx.*",
                "META-INF/proguard/androidx-*"
            )
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.compose
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

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
            groupId = LibConfig.groupId
            artifactId = LibConfig.artifactId
            version = LibConfig.version
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
    implementation(libs.kotlinStdLib)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.recyclerView)
    implementation(libs.constraintLayout)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}