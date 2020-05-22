plugins {
    id("java")
    id("java-library")
    kotlin("multiplatform")
}

kotlin {
    targets {
        jvm {
            compilations.all {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
        }
    }

    sourceSets {
        val jvmMain by getting {
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api(project(":kold-core"))
                implementation("org.springframework.boot:spring-boot-starter:2.3.0.RELEASE")
                implementation("com.fasterxml.jackson.core:jackson-core:2.11.0")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.11.0")
            }
        }

        val jvmTest by getting {
            kotlin.srcDir("src/test/kotlin")
            dependsOn(jvmMain)
            dependencies {
                implementation("io.kotest:kotest-runner-junit5-jvm:4.0.3")
                implementation("io.kotest:kotest-assertions-core-jvm:4.0.3")
                implementation("io.kotest:kotest-property-jvm:4.0.3")
            }

        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

apply(from = "../publish-sonatype.gradle.kts")