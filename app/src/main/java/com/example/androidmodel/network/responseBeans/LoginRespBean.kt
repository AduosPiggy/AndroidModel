package com.example.androidmodel.network.responseBeans

/**
 * @author kfflso
 * @data 2025-05-06 16:09
 * @plus:
 */
data class LoginRespBean(
    val accessToken: String,
    val avatarUrl: String,
    val expiresAt: Int,
    val nickName: String,
    val refreshToken: String,
    val userId: String,
    val userName: String
)