plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.9.22"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("nu.studer.credentials") version "3.0"
}

group = "io.github.aip-webdev"
version = "1.0"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}

dependencies {
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

gradlePlugin {
    website = "https://github.com/aip-webdev/gradle-in-docker-teamcity-plugin"
    vcsUrl = "https://github.com/aip-webdev/gradle-in-docker-teamcity-plugin.git"
    plugins {
        create("gradle-in-docker-ci") {
            id = "io.github.aip-webdev.gradle-in-docker-ci"
            implementationClass = "io.github.aip-webdev.DockerInGradleTeamcityPlugin"
            displayName = "Gradle plugin for displaying Teamcity test results"
            description = "Allows you to generate test outputs in docker in the format of Teamcity service messages " +
                "for accurate display of test results in Teamcity"
            tags = listOf("teamcity", "service messages", "docker", "test output", "retry")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
