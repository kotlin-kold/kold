import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    java
    id("java-library")
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "0.10.1"
    kotlin("multiplatform") version "1.3.72"
}

allprojects {
    version = "0.1"
    repositories {
        mavenLocal()
        mavenCentral()
    }

    group = "com.github.kotlin-kold"
}
