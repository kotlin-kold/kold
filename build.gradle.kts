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
}

allprojects {
    version = "0.2"
    repositories {
        mavenLocal()
        mavenCentral()
    }

    group = "com.github.kotlin-kold"
}
