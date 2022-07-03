package net.meilcli.rippletext.gradle.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.gradle.plugins.signing.SigningExtension
import java.io.File
import java.net.URI
import java.util.*

class MavenPublishPlugin : Plugin<Project> {

    private val initialVersion = "0.0.1"

    @Suppress("UnstableApiUsage")
    override fun apply(project: Project) {
        addArchives(project)

        val extension = checkNotNull(project.extensions.findByType(PublishingExtension::class.java))

        val versionPropertyFile = File(project.rootProject.rootDir, "buildSrc/version.properties")
        val versionProperty = Properties()
        val versionValue = if (versionPropertyFile.exists()) {
            versionProperty.load(versionPropertyFile.inputStream())
            versionProperty.getProperty("version") ?: initialVersion
        } else {
            initialVersion
        }

        val signingPropertyFile = File(project.rootProject.rootDir, "buildSrc/signing.properties")
        val signingProperty = Properties()
        if (signingPropertyFile.exists()) {
            signingProperty.load(signingPropertyFile.inputStream())
        }
        val sonatypeUsername = signingProperty.getProperty("sonatypeUsername")
        val sonatypePassword = signingProperty.getProperty("sonatypePassword")
        val signingKeyId = signingProperty.getProperty("signing.keyId")
        val signingPassword = signingProperty.getProperty("signing.password")
        val signingSecretKeyRingFile = signingProperty.getProperty("signing.secretKeyRingFile")
        if (signingKeyId.isNullOrEmpty().not() && signingPassword.isNullOrEmpty()
                .not() && signingSecretKeyRingFile.isNullOrEmpty().not()
        ) {
            project.gradle.taskGraph.whenReady {
                project.allprojects {
                    extra["signing.keyId"] = signingKeyId
                    extra["signing.password"] = signingPassword
                    extra["signing.secretKeyRingFile"] = signingSecretKeyRingFile
                }
            }
        }

        extension.repositories {
            maven {
                name = "Central"
                url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }

        project.plugins.findPlugin(JavaGradlePluginPlugin::class.java)

        project.afterEvaluate {
            extension.publications {
                // escape multiple attach Publication
                val attach: (MavenPublication) -> Unit = {
                    it.groupId = "net.meilcli.rippletext"
                    it.version = versionValue

                    it.pom {
                        name.set("RippleText")
                        description.set("Ripple effect for clickable text on android view and composable function")
                        url.set("https://github.com/MeilCli/RippleText")
                        licenses {
                            license {
                                name.set("MIT License")
                                url.set("https://github.com/MeilCli/RippleText/blob/master/LICENSE.txt")
                            }
                        }
                        developers {
                            developer {
                                id.set("MeilCli")
                                name.set("MeilCli")
                            }
                        }
                        scm {
                            connection.set("git@github.com:MeilCli/RippleText.git")
                            developerConnection.set("git@github.com:MeilCli/RippleText.git")
                            url.set("https://github.com/MeilCli/RippleText")
                        }
                    }

                    it.artifact(project.tasks["androidSourcesJar"])
                    it.artifact(project.tasks["androidJavadocsJar"])
                    it.from(project.components["release"])
                }

                register("central", MavenPublication::class.java) {
                    attach(this)
                    checkNotNull(project.extensions.findByType(SigningExtension::class.java)).sign(this)
                }
            }
        }
    }

    private fun addArchives(project: Project) {
        val androidExtension = checkNotNull(project.extensions.findByName("android") as? LibraryExtension)
        project.tasks.create("androidSourcesJar", Jar::class.java) {
            archiveClassifier.set("sources")
            from(androidExtension.sourceSets["main"].java.srcDirs)
        }

        project.tasks.create("androidJavadocsJar", Jar::class.java) {
            dependsOn("dokkaJavadoc")
            archiveClassifier.set("javadoc")
            from(File(project.buildDir, "docs/javadoc"))
        }
    }
}