package io.github.aipwebdev

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType

class TeamcityTestListener(private val supportRetry: Boolean = false) : TestListener {
    override fun beforeSuite(suite: TestDescriptor) {
        if (supportRetry && suite.name.contains("Suite")) {
            println("""##teamcity[testRetrySupport enabled='true']""")
        }
        println("##teamcity[testSuiteStarted name='${teamCityEscape(suite.name)}']")
    }

    override fun beforeTest(testDescriptor: TestDescriptor) {
        println("##teamcity[testStarted name='${teamCityEscape(testDescriptor.name)}']")
    }

    override fun afterTest(
        testDescriptor: TestDescriptor,
        result: TestResult,
    ) {
        val escapedTestName = teamCityEscape(testDescriptor.name)
        when (result.resultType) {
            ResultType.FAILURE ->
                println(
                    "##teamcity[testFailed name='$escapedTestName' message='FAILED' " +
                        "details='${teamCityEscape(result.exceptions.toString())}']",
                )
            ResultType.SKIPPED -> println("##teamcity[testIgnored name='$escapedTestName' message='${result.resultType}']")
            else -> {} // TC assumes the test is successful
        }
        println("##teamcity[testFinished name='$escapedTestName' duration='${result.endTime - result.startTime}']")
    }

    override fun afterSuite(
        suite: TestDescriptor,
        result: TestResult,
    ) {
        println("##teamcity[testSuiteFinished name='${teamCityEscape(suite.name)}' duration='${result.endTime - result.startTime}']")
    }

    private fun teamCityEscape(s: String): String {
        return s.replace("'", "|'")
            .replace("\n", "|n")
            .replace("\r", "|r")
            .replace("[", "|[")
            .replace("]", "|]")
    }
}
