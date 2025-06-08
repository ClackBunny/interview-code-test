package com.hacker

import com.hacker.db.DatabaseFactory
import com.hacker.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    // 获取配置文件路径
    val configFile = getConfigFile()
    println("Config file path: ${configFile.absolutePath}")

    // 尝试从外部配置文件读取端口
    val externalPort = loadPortFromFile(configFile)

    if (externalPort != null) {
        // 使用外部端口启动
        embeddedServer(Netty, port = externalPort, module = Application::module).start(wait = true)
    } else {
        // 回退到默认启动方式
        EngineMain.main(args)
    }
}

/**
 * 智能获取配置文件位置
 * 1. 首先尝试从EXE/JAR所在目录查找
 * 2. 然后尝试从工作目录查找
 * 3. 最后尝试从类路径查找
 */
fun getConfigFile(): File {
    // 方案1：获取EXE/JAR所在目录
    val exeDir = try {
        Paths.get(Application::class.java.protectionDomain.codeSource.location.toURI())
            .parent.toFile()
    } catch (e: Exception) {
        null
    }

    // 方案2：获取工作目录
    val workingDir = File(System.getProperty("user.dir"))

    // 优先检查EXE/JAR目录
    val possiblePaths = listOf(
        exeDir?.let { File(it, "application.yaml") },
        File(workingDir, "application.yaml")
    ).filterNotNull()

    // 查找第一个存在的配置文件
    return possiblePaths.firstOrNull { it.exists() } ?: run {
        println("Warning: Config file not found in: ${possiblePaths.joinToString { it.absolutePath }}")
        File(workingDir, "application.yaml") // 返回默认路径，即使文件不存在
    }
}

/**
 * 从指定文件加载端口配置
 */
private fun loadPortFromFile(configFile: File): Int? {
    return try {
        if (configFile.exists()) {
            // 解析YAML文件
            val yaml = Yaml()
            val config = yaml.load<Map<String, Any>>(configFile.inputStream())

            // 获取端口配置
            config?.get("ktor")?.let { ktorConfig ->
                (ktorConfig as? Map<*, *>)?.get("deployment")?.let { deploymentConfig ->
                    (deploymentConfig as? Map<*, *>)?.get("port")?.toString()?.toInt()
                }
            } ?: run {
                println("External config found but missing 'ktor.deployment.port'")
                null
            }
        } else {
            println("Config file not found at ${configFile.absolutePath}")
            null
        }
    } catch (e: Exception) {
        println("Error loading config: ${e.message}")
        null
    }
}

fun Application.module() {
    environment.log.info("Loaded port = ${environment.config.propertyOrNull("ktor.deployment.port")?.getString()}")
    DatabaseFactory.init()
    AuthService.initAdminUser()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
