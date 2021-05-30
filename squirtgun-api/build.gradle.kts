import org.apache.tools.ant.filters.ReplaceTokens

description = "squirtgun-api"

plugins {
    java
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")

    testImplementation("net.kyori:adventure-api:4.7.0")
    testImplementation("net.kyori:adventure-text-serializer-gson:4.7.0")
    testImplementation("net.kyori:adventure-text-serializer-legacy:4.7.0")
    testImplementation("com.google.guava:guava:30.1.1-jre")

    compileOnly("net.kyori:adventure-api:4.7.0")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly("com.google.code.gson:gson:2.8.6")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.7.0")
}

tasks {
    withType<ProcessResources> {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
    withType<Test> {
        useJUnitPlatform()
    }
}