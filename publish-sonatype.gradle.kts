apply(plugin = "java")
apply(plugin = "java-library")
apply(plugin = "maven-publish")
apply(plugin = "signing")
apply(plugin = "org.jetbrains.dokka")

repositories {
    mavenCentral()
}

val ossrhUsername: String by project
val ossrhPassword: String by project

fun Project.publishing(action: PublishingExtension.() -> Unit) =
    configure(action)

fun Project.signing(configure: SigningExtension.() -> Unit): Unit =
    configure(configure)

val dokka = tasks.named("dokka")

val publications: PublicationContainer = (extensions.getByName("publishing") as PublishingExtension).publications

signing {
    useGpgCmd()
    if (!version.toString().endsWith("SNAPSHOT"))
        sign(publications)
}

// Create dokka Jar task from dokka task output
val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(dokka)
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            name = "sonatype"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }

    publications.withType<MavenPublication>().forEach {
        it.apply {
            artifact(dokkaJar)
            pom {
                name.set("Kold")
                description.set("Validation library for Kotlin")
                url.set("https://github.com/kotlin-kold/kold")

                scm {
                    connection.set("scm:git:https://github.com/kotlin-kold/kold")
                    developerConnection.set("scm:git:https://github.com/kotlin-kold")
                    url.set("https://github.com/kotlin-kold/kold")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("paralainer")
                        name.set("Sergey Talov")
                        email.set("serg.talov@gmail.com")
                    }
                }
            }
        }
    }
}
