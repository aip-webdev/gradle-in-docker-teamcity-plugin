
@file:Suppress("ktlint:standard:package-name")

package io.github.`aip-webdev`

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType

class TeamcityTestListener(
    private val supportRetry: Boolean = false,
) : TestListener {
    private var suiteName = ""
    private var retryMessageSent = false

    override fun beforeSuite(suite: TestDescriptor) {
        defineSuiteOrClassName(suite)
        if (suiteName.isNotBlank()) {
            println("##teamcity[testSuiteStarted name='${teamCityEscape(suiteName)}']")
            if (supportRetry && !retryMessageSent) {
                println("""##teamcity[testRetrySupport enabled='true']""")
                retryMessageSent = true
            }
        }
    }

    override fun beforeTest(testDescriptor: TestDescriptor) {
        println("##teamcity[testStarted ${getTestInfo(testDescriptor)}]")
    }

    override fun afterTest(
        testDescriptor: TestDescriptor,
        result: TestResult,
    ) {
        when (result.resultType) {
            ResultType.FAILURE ->
                println(
                    "##teamcity[testFailed ${getTestInfo(testDescriptor)} message='FAILED' " +
                        "details='${teamCityEscape(result.exceptions.toString())}']",
                )
            ResultType.SKIPPED ->
                println("##teamcity[testIgnored ${getTestInfo(testDescriptor)} message='${result.resultType}']")
            else -> {}
        }
        println("##teamcity[testFinished ${getTestInfo(testDescriptor)} duration='${result.endTime - result.startTime}']")
    }

    override fun afterSuite(
        suite: TestDescriptor,
        result: TestResult,
    ) {
        defineSuiteOrClassName(suite)
        if (suiteName.isNotBlank()) {
            println("##teamcity[testSuiteFinished name='${teamCityEscape(suiteName)}' duration='${result.endTime - result.startTime}']")
        }
    }

    private fun teamCityEscape(s: String): String {
        return s.replace("'", "|'")
            .replace("\n", "|n")
            .replace("\r", "|r")
            .replace("[", "|[")
            .replace("]", "|]")
    }

    private fun defineSuiteOrClassName(suite: TestDescriptor) {
        suiteName =
            when {
                suite.name.contains("Suite") -> suite.name.split(".").last()
                // Чтобы testSuiteStarted выводилось только один раз за прогон, берём за suite класс идущий после Gradle Test Executor
                suite.name.contains(".") && suite.parent?.name?.contains("Gradle")!! -> suite.name.split(".").last()
                else -> ""
            }
    }

    private fun getTestInfo(testDescriptor: TestDescriptor): String {
        val name = teamCityEscape(testDescriptor.name)
        // для сортировки тестов по имени класса, пакету
        val className = testDescriptor.className
        return "name='${className?.let { "$it." }}$name'"
    }
}
