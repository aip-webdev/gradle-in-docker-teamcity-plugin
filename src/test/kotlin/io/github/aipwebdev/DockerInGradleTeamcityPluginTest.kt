package io.github.aipwebdev

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class DockerInGradleTeamcityPluginTest {
    private lateinit var tempDir: File
    private val someVersion = "1.2"

    @BeforeEach
    internal fun setUp() {
        tempDir = createTempDir()
    }

    @Test
    internal fun `default envVarName`() {
        val defaultVarName = "TEAMCITY_VERSION"
        System.setProperty(defaultVarName, someVersion)

        File(tempDir, "build.gradle").run {
            writeText(
                """
                plugins {
                  id "io.github.aipwebdev.gradle-in-docker-ci"
                }
                """.trimIndent(),
            )
        }

        val buildResult =
            GradleRunner.create()
                .withProjectDir(tempDir)
                .withPluginClasspath()
                .withArguments("tasks")
                .build()

        assertThat(buildResult.output)
            .contains("Applying TeamcityTestListener with $defaultVarName = $someVersion")
    }

    @Test
    internal fun `changed envVarName`() {
        val changedEnvVarName = "SOME_VAR"
        System.setProperty(changedEnvVarName, someVersion)

        File(tempDir, "build.gradle").run {
            writeText(
                """
                plugins {
                  id "io.github.aipwebdev.gradle-in-docker-ci"
                }

                dockerTeamcity {
                  teamcity = "$changedEnvVarName"
                }
                """.trimIndent(),
            )
        }

        val buildResult =
            GradleRunner.create()
                .withProjectDir(tempDir)
                .withPluginClasspath()
                .withArguments("tasks")
                .build()

        assertThat(buildResult.output)
            .contains("Applying TeamcityTestListener with $changedEnvVarName = $someVersion")
    }
}
