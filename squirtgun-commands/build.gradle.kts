description = "squirtgun-commands"

plugins {
    java
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
    testImplementation(project(":squirtgun-api"))

    compileOnly("net.kyori:adventure-api:4.7.0")
    compileOnly("com.google.guava:guava:30.1.1-jre")
    compileOnly(project(":squirtgun-api"))
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}