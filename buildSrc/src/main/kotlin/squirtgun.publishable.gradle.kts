plugins {
    `java-library`
    `maven-publish`
    signing
    checkstyle
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()
}

lateinit var publication: MavenPublication

publishing {
    publications {
        publication = create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                description.set("A multipurpose library designed for Minecraft: Java Edition plugins.")
                url.set("https://lucypoulton.net")
                name.set("squirtgun")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        id.set("lucyy-mc")
                        name.set("Lucy Poulton")
                        email.set("lucy@lucypoulton.net")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/lucyy-mc/Squirtgun.git")
                    developerConnection.set("scm:git:git://github.com/lucyy-mc/Squirtgun.git")
                    url.set("https://github.com/lucyy-mc/Squirtgun")
                }
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

            if (ossrhUsername != null && ossrhPassword != null) {
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }

        javadoc { }
    }


    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
        if (signatory == null) {
            logger.warn("No signatories available, skipping signing.")
        } else {
            sign(publication)
        }
    }
}