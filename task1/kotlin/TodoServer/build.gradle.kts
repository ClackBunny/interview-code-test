val kotlin_version: String by project
val logback_version: String by project
val exposed_version:String by project
val sqlite_version:String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.3"
}

group = "com.hacker"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
//    mavenLocal()

    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    maven { url = uri("https://maven.aliyun.com/repository/spring/") }
    maven { url = uri("https://maven.aliyun.com/repository/google/") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }
    maven { url = uri("https://maven.aliyun.com/repository/spring-plugin/") }
    maven { url = uri("https://maven.aliyun.com/repository/grails-core/") }
    maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots/") }

//    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-jackson")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.ktor:ktor-server-status-pages")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // SQLite JDBC 驱动
    implementation("org.xerial:sqlite-jdbc:$sqlite_version")
}
