@file:Suppress("ktlint:standard:package-name")

package io.github.`aip-webdev`

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class DockerInGradleTeamcityPluginTest {
    private lateinit var tempDir: File

    @BeforeEach
    internal fun setUp() {
        tempDir = createTempDir()
    }

    @Test
    internal fun `default envVarName`() {
        val defaultFlag = true

        File(tempDir, "build.gradle").run {
            writeText(
                """
                plugins {
                  id "io.github.aip-webdev.gradle-in-docker-ci"
                }
                gradleInDocker {
                  teamcity = $defaultFlag
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
            .contains("Applying io.github.`aip-webdev`.TeamcityTestListener with teamcity = $defaultFlag")
    }

    @Test
    internal fun `changed envVarName`() {
        val changedFlag = false

        File(tempDir, "build.gradle").run {
            writeText(
                """
                plugins {
                  id "io.github.aip-webdev.gradle-in-docker-ci"
                }
                gradleInDocker {
                  teamcity = $changedFlag
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

        assertThat("Applying io.github.`aip-webdev`.TeamcityTestListener with teamcity = $changedFlag")
            .isNotIn(buildResult.output)
    }
}
