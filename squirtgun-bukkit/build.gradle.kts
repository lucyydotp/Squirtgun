group = "me.lucyy"
description = "Squirtgun"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.7.0")
    compileOnly(project(":squirtgun-api"))
}