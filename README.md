# docker-in-gradle-teamcity-plugin
Gradle plugin that enables the production of test output in the Teamcity Service Message format.

This plugin is intended for use when your build occurs within a Docker container on Teamcity. In such situations, populating the test report to Teamcity is essential for obtaining accurate test results.

When the TEAMCITY_VERSION environment variable is present, enabling this plugin will produce test results in the [Teamcity Service Message](https://confluence.jetbrains.com/display/TCD18/Build+Script+Interaction+with+TeamCity) format.

The plugin is based on [docker-gradle-teamcity-plugin](https://github.com/sa1nt/docker-gradle-teamcity-plugin) and includes updated dependencies and support for retrying failed tests, providing more accurate test counting. Additionally, the display of suite classname and package has been enhanced.

## Usage
See [Gradle Plugin portal](https://plugins.gradle.org/plugin/io.github.aip-webdev.gradle-in-docker-ci)

### `build.gradle`
```groovy
plugins {
  id "io.github.aip-webdev.gradle-in-docker-ci" version "1.0"
}
```

### `build.gradle.kts`
```kotlin
plugins {
    id("io.github.aip-webdev.gradle-in-docker-ci") version "1.0"
}
```

### Optionally
You can override the env variable name the plugin uses to determine whether we're on Teamcity by

### `build.gradle` or `build.gradle.kts`
```groovy
gradleInDocker {
    teamcity = true
    supportRetry = true 
}
```
