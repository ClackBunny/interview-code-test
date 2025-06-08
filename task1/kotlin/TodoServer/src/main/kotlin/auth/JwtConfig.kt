package com.hacker.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    // JWT 签名密钥（生产环境请放到配置文件中）
    private const val secret = "Mi6=Bf8ZeI~t1,bD"
    private const val issuer = "404notfound.cn" // 签发者名称
    private const val audience = "ktor-audience" // 受众
    private const val validityInMs = 36_000_00 * 10 // JWT 有效期：10 小时

    // 使用 HMAC256 算法生成签名
    private val algorithm = Algorithm.HMAC256(secret)

    // 用于验证 JWT 的 verifier 对象
    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    // 根据用户名生成 JWT Token
    fun generateToken(username: String,passwordVersion:Int): String =
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", username)
            .withClaim("passwordVersion", passwordVersion)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(algorithm)
}
