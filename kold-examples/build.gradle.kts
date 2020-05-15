plugins {
    id("kotlin-multiplatform")
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
            kotlin.srcDir("src/main")
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api(project(":kold-core"))
            }
        }
    }
}
