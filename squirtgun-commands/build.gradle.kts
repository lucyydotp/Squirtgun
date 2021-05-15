description = "squirtgun-commands"


java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
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

publishing.publications {
    create<MavenPublication>("mavenJava").from(components["java"])
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}