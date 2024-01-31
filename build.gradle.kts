

plugins {
	`java-gradle-plugin`
	kotlin("jvm") version "1.9.22"
	id("com.gradle.plugin-publish") version "1.2.1"
	id("nu.studer.credentials") version "3.0"
}

group = "io.github.aipwebdev"
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
	testImplementation("org.assertj:assertj-core:3.9.0")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

gradlePlugin {
	website = "https://github.com/aip-webdev/gradle-in-docker-teamcity-plugin"
	vcsUrl = "https://github.com/aip-webdev/gradle-in-docker-teamcity-plugin.git"
	plugins {
		create("gradle-in-docker-ci") {
			id = "io.github.aipwebdev.gradle-in-docker-ci"
			implementationClass = "io.github.aipwebdev.DockerInGradleTeamcityPlugin"
			displayName = "Plugin to produce test output as Teamcity Service Messages with retry"
			description = "Produces tests' output in Teamcity Service Message format with retry"
			tags = listOf("teamcity", "service messages", "docker", "test output", "retry")
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

