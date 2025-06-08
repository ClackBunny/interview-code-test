package com.hacker

import com.hacker.db.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.yaml.snakeyaml.Yaml
import java.io.File

fun main(args: Array<String>) {
    // 尝试从外部配置文件读取端口
    val externalPort = loadPortFromExternalConfig()

    if (externalPort != null) {
        // 使用外部端口启动
        embeddedServer(Netty, port = externalPort, module = Application::module).start(wait = true)
    } else {
        // 回退到默认启动方式
        EngineMain.main(args)
    }
}

private fun loadPortFromExternalConfig(): Int? {
    return try {
        // 获取JAR所在目录
        val jarDir = File(System.getProperty("user.dir"))
        val configFile = File(jarDir, "application.yaml")

        if (configFile.exists()) {
            // 解析YAML文件
            val yaml = Yaml()
            val config = yaml.load<Map<String, Any>>(configFile.inputStream())

            // 获取端口配置（支持多级嵌套）
            config?.get("ktor")?.let { ktorConfig ->
                (ktorConfig as? Map<*, *>)?.get("deployment")?.let { deploymentConfig ->
                    (deploymentConfig as? Map<*, *>)?.get("port")?.toString()?.toInt()
                }
            } ?: run {
                println("External config found but missing 'ktor.deployment.port'")
                null
            }
        } else {
            println("No external config found at ${configFile.absolutePath}")
            null
        }
    } catch (e: Exception) {
        println("Error loading external config: ${e.message}")
        null
    }
}

fun Application.module() {
    environment.log.info("Loaded port = ${environment.config.propertyOrNull("ktor.deployment.port")?.getString()}")
    DatabaseFactory.init()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
