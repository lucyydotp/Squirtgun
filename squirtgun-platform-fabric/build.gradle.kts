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

// For changing versions, upgrading etc: https://fabricmc.net/versions.html
plugins {
    id("fabric-loom") version "0.9-SNAPSHOT"
    `java-library`
    `maven-publish`
    signing
    checkstyle
}

val minecraftVersion: String = "1.17.1"
val yarnBuild: Int = 61
val loaderVersion: String = "0.12.1"
val fabricApiVersion: String = "0.40.6+1.17"
val fabricPermissionsApiVersion: String = "0.1-SNAPSHOT"
val adventureFabricVersion: String = "4.1.0"

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.$yarnBuild:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    // Only depend on the necessary API
    // https://github.com/FabricMC/fabric
    sequenceOf(
        "lifecycle-events-v1",
        "networking-api-v1"
    ).forEach { modImplementation(fabricApi.module("fabric-$it", fabricApiVersion)) }

    modApi("me.lucko:fabric-permissions-api:$fabricPermissionsApiVersion")
    modImplementation("net.kyori:adventure-platform-fabric:$adventureFabricVersion")

    api(include(project(":squirtgun-api")) {
        exclude("net.kyori")
    })
}

java {
    withSourcesJar()
    withJavadocJar()
}

lateinit var publication: MavenPublication

publishing {
    publications {
        publication = create<MavenPublication>("mavenJava") {
            artifact(tasks.remapJar) { builtBy(tasks.remapJar) }
            //artifact(tasks.sourcesJar) { builtBy(tasks.remapSourcesJar) }
            //artifact(tasks.javadocJar) { builtBy(tasks.javadocJar) }

            pom {
                withXml {
                    val deps = asNode().appendNode("dependencies")
                    project(":squirtgun-api") {
                        val dep = deps.appendNode("dependency")
                        dep.appendNode("groupId", this.group)
                        dep.appendNode("artifactId", this.name)
                        dep.appendNode("version", this.version)
                        dep.appendNode("scope", "compile")
                    }
                }

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
    } else {
        sign(publication)
    }
}
