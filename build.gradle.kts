import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("maven-publish")
    id("signing")
}


group = "me.lucyy"
version = "2.0.0-SNAPSHOT"
description = "LucyCommonLib"

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")

    testImplementation("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    testImplementation("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    testImplementation("net.kyori:adventure-api:4.7.0")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.7.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                description.set("Common functions for use in Bukkit plugins")
                url.set("https://lucyy.me")
                name.set("LucyCommonLib")
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
                    connection.set("scm:git:git://github.com/lucyy-mc/lucycommonlib.git")
                    developerConnection.set("scm:git:git://github.com/lucyy-mc/lucycommonlib.git")
                    url.set("https://github.com/lucyy-mc/lucycommonlib")
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

            if (ossrhUsername != null && ossrhPassword != null)
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<ProcessResources> {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
    withType<Test> {
        useJUnitPlatform()
    }

    withType<Javadoc> {
        val opts = options as StandardJavadocDocletOptions
        opts.stylesheetFile = File("${project.rootDir}/src/main/javadoc/resources/javadoc.css")
        opts.addBooleanOption("-allow-script-in-comments", true)
        opts.footer = "<script src=\"{@docRoot}/resources/prism.js\" type=\"text/javascript\"></script>"
        doLast {
            copy {
                from("${project.rootDir}/src/main/javadoc/resources")
                into("${buildDir}/docs/javadoc/resources")
                include("prism.js", "prism.css")
            }
        }
    }
}

task("printVersion") {
    println("VERSION=" + project.version)
}