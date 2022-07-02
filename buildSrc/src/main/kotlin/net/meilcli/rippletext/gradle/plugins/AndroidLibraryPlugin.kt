package net.meilcli.rippletext.gradle.plugins

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import net.meilcli.rippletext.gradle.dependencies.Android
import net.meilcli.rippletext.gradle.dependencies.Junit4
import net.meilcli.rippletext.gradle.dependencies.Kotlin
import net.meilcli.rippletext.gradle.extensions.androidTestImplementation
import net.meilcli.rippletext.gradle.extensions.api
import net.meilcli.rippletext.gradle.extensions.testImplementation
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.findByName("android") as BaseExtension
        extension.compileSdkVersion(32)

        extension.defaultConfig {
            minSdk = 21
            targetSdk = 32
            versionCode = 1
            versionName = "1.0"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFile("consumer-rules.pro")
        }

        val defaultProguard = getDefaultProguardFile("proguard-android-optimize.txt", project.layout.buildDirectory)
        (extension.buildTypes.findByName("release")
            ?: extension.buildTypes.create("release")).apply {
            isMinifyEnabled = false
            proguardFiles(defaultProguard, "proguard-rules.pro")
        }

        extension.sourceSets.all {
            java.srcDir("src/${name}/kotlin")
        }
        extension.compileOptions {
            sourceCompatibility(JavaVersion.VERSION_1_8)
            targetCompatibility(JavaVersion.VERSION_1_8)
        }
        project.tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjvm-default=all-compatibility")
            }
        }

        project.dependencies {
            api(Kotlin.stdlib)
            api(Android.core)

            testImplementation(Junit4.junit)

            androidTestImplementation(Android.espresso)
            androidTestImplementation(Android.junit)
        }
    }
}