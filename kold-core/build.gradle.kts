plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.71")
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
    target {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}