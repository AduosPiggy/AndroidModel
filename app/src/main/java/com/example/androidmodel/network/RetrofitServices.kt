package com.example.androidmodel.network

import com.example.androidmodel.network.requestBeans.LoginReqBean
import com.example.androidmodel.network.responseBeans.LoginRespBean
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author kfflso
 * @data 2025-05-06 16:16
 * @plus:
 */
interface RetrofitServices {
    @POST(Urls.LOGIN)
    suspend fun login(@Body body: LoginReqBean): BaseResp<LoginRespBean>
}