package com.example.androidmodel.network

/**
 * @author kfflso
 * @data 2025-05-06 17:18
 * @plus:
 */
/*数据解析扩展函数*/
fun <T> BaseResp<T>.dataConvert(): T {
    if (status == 1) {
        return data
    }  else {
        throw Exception(message)
    }
}