/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

plugins {
    `maven-publish`
    java
    signing
}

subprojects {
    version = "2.0.0-pre3"
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
                            name.set("MIT License")
                            url.set("https://www.opensource.org/licenses/mit-license.php")
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
             sign(publishing.publications["mavenJava"])
        }
        if (signatory == null) {
            logger.warn("No signatories available, skipping signing.")
        }
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<Javadoc>()
    }
}

task("printVersion") {
    println("VERSION=" + project.version)
}