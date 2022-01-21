plugins {
    `java-platform`
    `maven-publish`
}

repositories {
    mavenCentral()
}

javaPlatform.allowDependencies()

val squirtgunVersion = "2.0.0-pre8"
val adventureVersion = "4.9.3"
val gsonVersion = "2.8.0"
val guavaVersion = "21.0"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["javaPlatform"])
            pom {
                description.set("Squirtgun BOM")
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

dependencies {
    constraints {
        api("net.kyori:adventure-api:$adventureVersion")
        api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
        api("net.kyori:adventure-text-serializer-gson:$adventureVersion")
        api("net.kyori:adventure-text-serializer-plain:$adventureVersion")
        api("org.jetbrains:annotations:22.0.0")

        api("com.google.guava:guava:$guavaVersion")

        api("com.google.code.gson:gson:$gsonVersion") {
            because("minecraft uses ancient versions of gson")
        }
    }
}