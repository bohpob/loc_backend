val ktor_version: String by project
val mockk_version: String by project
val kotlin_version: String by project
val mockito_version: String by project
val logback_version: String by project
val exposed_version: String by project
val commons_codec_version: String by project
val junit_jupiter_version: String by project
val mockito_kotlin_version: String by project
val postgresql_jdbc_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "cz.cvut.fit.poberboh.loc_backend"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")

    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    implementation("org.postgresql:postgresql:$postgresql_jdbc_version")

    implementation("commons-codec:commons-codec:$commons_codec_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("org.mockito:mockito-junit-jupiter:3.12.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("org.mockito:mockito-core:$mockito_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockito_kotlin_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
}
