import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {
  //std lib
  private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

  //lifecycle
  private const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"

  //android ui
  private const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
  private const val constraintLayout =
    "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

  //libs
  private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
  private const val material = "com.google.android.material:material:${Versions.material}"

  val sampleLibraries = arrayListOf<String>().apply {
    add(kotlinStdLib)
    add(appcompat)
    add(material)
    add(constraintLayout)
  }

  val loggerLibraries = arrayListOf<String>().apply {
    add(kotlinStdLib)
    add(lifecycle)
    add(recyclerView)
  }
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
  list.forEach { dependency ->
    add("kapt", dependency)
  }
}

fun DependencyHandler.implementation(list: List<String>) {
  list.forEach { dependency ->
    add("implementation", dependency)
  }
}