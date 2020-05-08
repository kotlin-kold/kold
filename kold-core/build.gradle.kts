plugins {
    id("java")
    id("java-library")
    id("kotlin-multiplatform")
    `maven-publish`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.3")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.0.3")
    testImplementation("io.kotest:kotest-property-jvm:4.0.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
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
}

apply(from = "../publish-sonatype.gradle.kts")
