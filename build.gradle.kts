plugins {
    `maven-publish`
    java
    signing
}

subprojects {
    version = "2.0.0-SNAPSHOT"
    group = "me.lucyy"

    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
    apply<JavaPlugin>()

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        withJavadocJar()
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                pom {
                    artifactId = artifactId
                    description.set("A multipurpose library designed for Minecraft: Java Edition plugins.")
                    url.set("https://lucyy.me")
                    name.set("squirtgun")
                    licenses {
                        license {
                            name.set("GNU General Purpose License v3")
                            url.set("https://www.gnu.org/licenses/gpl-3.0-standalone.html")
                        }
                    }
                    developers {
                        developer {
                            id.set("lucyy-mc")
                            name.set("Lucy Poulton")
                            email.set("lucy@poulton.xyz")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/lucyy-mc/Squirtgun.git")
                        developerConnection.set("scm:git:git://github.com/lucyy-mc/Squirtgun.git")
                        url.set("https://github.com/lucyy-mc/Squirtgun")
                    }
                }
            }

            repositories {
                maven {
                    val releasesUri = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    val snapshotsUri = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUri else releasesUri

                    val ossrhUsername: String? by project
                    val ossrhPassword: String? by project

                    if (ossrhUsername != null && ossrhPassword != null)
                        credentials {
                            username = ossrhUsername
                            password = ossrhPassword
                        }
                }
            }
        }
    }

    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
        if (signatory == null) {
            logger.warn("No signatories available, skipping signing.")
        }
        sign(publishing.publications["mavenJava"])
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<Javadoc>()
    }
}