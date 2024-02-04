
@file:Suppress("ktlint:standard:package-name")

package io.github.`aip-webdev`

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

open class DockerInGradleTeamcityPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.run {
                create("gradleInDocker", GradleInDockerTeamcityExtension::class.java)
            }

        project.afterEvaluate {
            val teamcity = extension.teamcity
            val supportRetry = extension.supportRetry
            if (teamcity) {
                println("Applying io.github.`aip-webdev`.TeamcityTestListener with teamcity = true")
                project.tasks.withType(Test::class.java) { testTask ->
                    testTask.addTestListener(TeamcityTestListener(supportRetry))
                }
            }
        }
    }

    private fun getEnvironmentVariable(varName: String): String? {
        return System.getProperty(varName) ?: System.getenv(varName)
    }
}
