import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream

val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val sqlite_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.3"
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.hacker"
version = "0.0.1"

application {
    mainClass = "com.hacker.ApplicationKt"
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    maven { url = uri("https://maven.aliyun.com/repository/spring/") }
    maven { url = uri("https://maven.aliyun.com/repository/google/") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }
    maven { url = uri("https://maven.aliyun.com/repository/spring-plugin/") }
    maven { url = uri("https://maven.aliyun.com/repository/grails-core/") }
    maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots/") }

    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-jackson")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.3")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("io.ktor:ktor-server-status-pages")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // SQLite JDBC 驱动
    implementation("org.xerial:sqlite-jdbc:$sqlite_version")

    // validator
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("org.glassfish:jakarta.el:4.0.2")

}


// ================= 提取公共路径 =================
val jlinkDir = layout.buildDirectory.dir("jlink")
val jpackageOutput = layout.buildDirectory.dir("jpackage")
val runtimeModulesFile = layout.buildDirectory.file("runtime-modules.txt")

// ================= 1. 配置 ShadowJar 任务 =================
tasks {
    // 1. 先确保 jar 任务的 Manifest 是干净的
    withType<Jar> {
        manifest {
            attributes(
                "Main-Class" to "com.hacker.ApplicationKt" // 显式设置
            )
        }
    }

    // 2. 强制 shadowJar 使用你的配置
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("TodoServer")
        archiveClassifier.set("")
        manifest {
            attributes(
                "Main-Class" to "com.hacker.ApplicationKt" // 再次确认
            )
        }
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
}

// ================= 2. 分析依赖模块 =================
tasks.register("jdepsModules") {
    group = "build"
    description = "分析应用程序的模块依赖"

    val shadowJarTask = tasks.named<ShadowJar>("shadowJar").get()
    dependsOn(shadowJarTask)

    inputs.file(shadowJarTask.archiveFile) // 输入声明
    outputs.file(runtimeModulesFile)      // 输出声明

    doLast {
        val jarFile = shadowJarTask.archiveFile.get().asFile
        val output = ByteArrayOutputStream().use { bos ->
            project.exec {
                commandLine("jdeps", "--print-module-deps", "--ignore-missing-deps", jarFile.absolutePath)
                standardOutput = bos
            }
            bos.toString().trim()
        }
        runtimeModulesFile.get().asFile.writeText(output)
    }
}

// ================= 3. 构建自定义运行时 =================
tasks.register("jlinkRuntime") {
    group = "build"
    description = "创建自定义JRE运行时"

    dependsOn(tasks.named("jdepsModules"))

    inputs.file(runtimeModulesFile)  // 输入声明
    outputs.dir(jlinkDir)            // 输出声明

    doLast {
        val modules = runtimeModulesFile.get().asFile.readText()
        val outputDir = jlinkDir.get().asFile

        // 清理旧运行时
        if (outputDir.exists()) outputDir.deleteRecursively()

        project.exec {
            commandLine(
                "jlink",
                "--output", outputDir.absolutePath,
                "--add-modules", modules,
                "--strip-debug",
                "--compress", "2",
                "--no-header-files",
                "--no-man-pages"
            )
        }
    }
}

// ================= 4. 打包应用程序 =================
tasks.register("jpackageInstaller") {
    group = "distribution"
    description = "创建应用程序安装包"

    dependsOn(tasks.named("jlinkRuntime"))
    val shadowJarTask = tasks.named<ShadowJar>("shadowJar").get()
    dependsOn(shadowJarTask)

    inputs.dir(jlinkDir)                     // 输入声明
    inputs.file(shadowJarTask.archiveFile)   // 输入声明
    outputs.dir(jpackageOutput)              // 输出声明

    doLast {
        // 复制配置文件到输入目录
        copy {
            from("application.yaml")
            into(shadowJarTask.archiveFile.get().asFile.parent)
        }

        project.exec {
            commandLine(
                "jpackage",
                "--input", shadowJarTask.archiveFile.get().asFile.parent,
                "--main-jar", shadowJarTask.archiveFileName.get(),
                "--main-class", "com.hacker.ApplicationKt", // 必须改为你的主类
                "--name", "TodoServer",
                "--type", "app-image",
                "--win-console",
                "--runtime-image", jlinkDir.get().asFile.absolutePath,
                "--dest", jpackageOutput.get().asFile.absolutePath,
                "--resource-dir", shadowJarTask.archiveFile.get().asFile.parent, // 包含配置文件
            )
            isIgnoreExitValue = true
            standardOutput = System.out
            errorOutput = System.err
        }
    }
}

// ================= 5. 聚合任务 =================
tasks.register("packageApp") {
    group = "build"
    description = "完整构建流程：JAR → 模块分析 → JRE → 安装包"
    dependsOn(tasks.named("jpackageInstaller"))
}