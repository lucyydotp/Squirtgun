import org.apache.tools.ant.filters.ReplaceTokens

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

repositories {
    mavenCentral()
}

dependencies {
    val adventureVersion = "4.9.3"
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    testImplementation("net.kyori:adventure-api:$adventureVersion")
    testImplementation("net.kyori:adventure-text-serializer-gson:$adventureVersion")
    testImplementation("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
    @Suppress("GradlePackageUpdate")
    testImplementation("com.google.guava:guava:21.0") {
        because("It's the version Minecraft is bundled with")
    }

    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")

    compileOnlyApi("com.google.guava:guava:21.0") {
        because("It's the version Minecraft is bundled with")
    }
    @Suppress("GradlePackageUpdate")
    compileOnlyApi("com.google.code.gson:gson:2.8.0") {
        because("It's the version Minecraft is bundled with")
    }
    compileOnlyApi("org.jetbrains:annotations:22.0.0")
}

tasks {
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
    test {
        useJUnitPlatform()
    }
}
