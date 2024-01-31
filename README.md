# docker-gradle-teamcity-plugin
Gradle plugin to produce test output in Teamcity Service Message format, inspired by [Jest Teamcity reporter](https://github.com/winterbe/jest-teamcity-reporter).

The idea is that when your build happens inside a Docker container on Teamcity, you generally need to populate the test report to Teamcity so it could show you test results.

Alternatively you can enable this plugin, which, if `TEAMCITY_VERSION` env variable is present, will produce test results in the [Teamcity Service Message](https://confluence.jetbrains.com/display/TCD18/Build+Script+Interaction+with+TeamCity) format.

The plugin is based on an existing plugin, dependencies have been updated, and support for retry tests has been added.

## Usage
See [Gradle Plugin portal](https://plugins.gradle.org/plugin/io.github.aipwebdev.gradle-in-docker-ci)

### `build.gradle`
```groovy
plugins {
  id "io.github.aipwebdev.gradle-in-docker-ci" version "1.0"
}
```

### `build.gradle.kts`
```kotlin
plugins {
    id("io.github.aipwebdev.gradle-in-docker-ci") version "1.0"
}
```

### Optionally
You can override the env variable name the plugin uses to determine whether we're on Teamcity by

### `build.gradle` or `build.gradle.kts`
```groovy
dockerTeamcity {
    teamcity = "NOT_EMPTY_VALUE"
    supportRetry = "true"  // <--- 'true' or 'false'
}
```
