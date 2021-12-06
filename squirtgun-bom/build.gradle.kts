plugins {
    `java-platform`
}

repositories {
    mavenCentral()
}

javaPlatform.allowDependencies()

val squirtgunVersion = "2.0.0-pre8"
val adventureVersion = "4.9.3"
val gsonVersion = "2.8.0"
val guavaVersion = "21.0"

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