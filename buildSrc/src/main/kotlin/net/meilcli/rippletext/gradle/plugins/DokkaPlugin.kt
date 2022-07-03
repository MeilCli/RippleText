package net.meilcli.rippletext.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.File

class DokkaPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.withType(DokkaTask::class.java) {
            outputDirectory.set(File(project.buildDir, "docs/javadoc"))
        }
    }
}