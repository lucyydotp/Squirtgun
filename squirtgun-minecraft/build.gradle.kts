plugins {
    id("squirtgun.publishable")
}
repositories {
    mavenCentral()
}

dependencies {
    api(project(":squirtgun-api"))
    api("net.kyori:adventure-api")
    compileOnly("org.jetbrains:annotations")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    testImplementation("net.kyori:adventure-api:")
    testImplementation("net.kyori:adventure-text-serializer-gson")
    testImplementation("net.kyori:adventure-text-serializer-legacy")
    testImplementation("com.google.guava:guava")
}

tasks.test {
    useJUnitPlatform()
}