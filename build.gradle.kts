plugins {
    kotlin("multiplatform") version "1.6.10"

    // ktlint
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"

    // kotest
    id("io.kotest.multiplatform") version "5.1.0"
}

group = "space.jgkang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.1.0")
                implementation("io.kotest:kotest-assertions-core:5.1.0")
            }
        }
    }
}
