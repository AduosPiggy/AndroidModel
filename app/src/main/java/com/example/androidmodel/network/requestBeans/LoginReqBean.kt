package com.example.androidmodel.network.requestBeans

/**
 * @author kfflso
 * @data 2025-05-06 16:07
 * @plus:
 */
data class LoginReqBean(
    val fromPlat: String,
    val loginType: Int,
    val password: String,
    val userName: String,
    val typeCode:Int
)
