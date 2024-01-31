package io.github.aipwebdev

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

open class DockerInGradleTeamcityPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.run {
                create("dockerTeamcity", DockerInGradleTeamcityExtension::class.java)
            }

        project.afterEvaluate {
            val teamcity = getEnvironmentVariable(extension.teamcity)
            val supportRetry = getEnvironmentVariable(extension.supportRetry) == "true"
            if (!teamcity.isNullOrEmpty()) {
                println("Applying TeamcityTestListener with ${extension.teamcity} = $teamcity")
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
