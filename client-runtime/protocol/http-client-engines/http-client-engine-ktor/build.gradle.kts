/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0.
 */

description = "Ktor HTTP Client Engine for Smithy services generated by smithy-kotlin"
extra["displayName"] = "Smithy :: Kotlin :: HTTP :: Engine :: Ktor"
extra["moduleName"] = "software.aws.clientrt.http.engine.ktor"

val ktorVersion: String by project

kotlin {
    sourceSets {
        jvmMain {
            dependencies {
                api(project(":client-runtime:protocol:http"))
                implementation(project(":client-runtime:io"))
                // okhttp works on both JVM and Android
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation(project(":client-runtime:logging"))
            }
        }
    }
}
