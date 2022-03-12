import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application

    // ktlint
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = "space.jgkang"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    // Add manifest.
    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    /**
     * Add all dependencies.
     * @see <a href="https://docs.gradle.org/current/userguide/working_with_files.html#sec:creating_uber_jar_example">
     *         Gradle Documentation - Creating "uber" or "fat" JARs
     *     </a>
     */
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith("jar")
        }.map {
            zipTree(it)
        }
    })

    // Remove duplicates.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
