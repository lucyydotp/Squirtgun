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

plugins {
    id("squirtgun.publishable")
}

repositories {
    mavenCentral()
}

dependencies {
    api(platform(project(":squirtgun-bom")))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    testImplementation("net.kyori:adventure-api:")
    testImplementation("net.kyori:adventure-text-serializer-gson")
    testImplementation("net.kyori:adventure-text-serializer-legacy")
    testImplementation("com.google.guava:guava")

    api("net.kyori:adventure-api")
    api("net.kyori:adventure-text-serializer-legacy")

    compileOnlyApi("com.google.guava:guava")
    compileOnlyApi("com.google.code.gson:gson")
    compileOnlyApi("org.jetbrains:annotations")
}

tasks {
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
    test {
        useJUnitPlatform()
    }
}
