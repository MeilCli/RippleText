plugins {
    `kotlin-dsl`
}

apply(from = "../dependencies/build.gradle")

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.github.gmazzo:gradle-buildconfig-plugin:3.1.0")
    }
}

repositories {
    mavenCentral()
    google()
}

val androidGradle = extra["library_Android_gradle"] as String
val kotlinGradle = extra["library_Kotlin_gradle"] as String
val dokkaPlugin = extra["library_Dokka_plugin"] as String

dependencies {
    implementation(androidGradle)
    implementation(kotlinGradle)
    implementation(dokkaPlugin)
}