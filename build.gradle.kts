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
    `java-library`
    `maven-publish`
    signing
    checkstyle
}

version = "2.0.0-pre8"
group = "net.lucypoulton"

subprojects {
    version = rootProject.version
    group = rootProject.group
    description = name

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "checkstyle")

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        withJavadocJar()
        withSourcesJar()
    }

    lateinit var publication: MavenPublication

    publishing {
        publications {
            // fabric has a bit of a special task: remapping
            if (name != "squirtgun-platform-fabric") {
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
        }

        if (name != "squirtgun-platform-fabric") {
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
    }

    tasks {
        processResources {
            eachFile { expand("version" to version) }
        }

        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }

        javadoc { }
    }
}

task("printVersion") {
    println("VERSION=" + project.version)
}
