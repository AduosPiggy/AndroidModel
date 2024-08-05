package com.example.androidmodel.beans

/**
 * @author kfflso
 * @data 2024/8/1/001 18:35
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