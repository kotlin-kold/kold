plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.71")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api(project(":kold-core"))
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