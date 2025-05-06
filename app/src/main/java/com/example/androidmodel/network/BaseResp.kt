package com.example.androidmodel.network

/**
 * @author kfflso
 * @data 2025-05-06 16:20
 * @plus:
 */
data class BaseResp<T>(
    var status:Int = 0,
    var message: String = "",
    var data: T
){
    fun isSuccess() = status == 1
}