package com.example.androidmodel.activities.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.example.androidmodel.base.BaseApp
import com.example.androidmodel.base.BaseViewModel
import com.example.androidmodel.network.RetrofitManager
import com.example.androidmodel.network.dataConvert
import com.example.androidmodel.network.requestBeans.LoginReqBean
import com.example.androidmodel.network.responseBeans.LoginRespBean
import com.example.androidmodel.tools.SPUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author kfflso
 * @data 2024/8/1/001 18:34
 * @plus:
 */
class LoginVM : BaseViewModel() {
    var loginRespBean = MutableLiveData<LoginRespBean>()
    suspend fun register(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val login = RetrofitManager.apiServices().login(
                    LoginReqBean(
                        "android", 1, password, username, 0
                    )
                ).dataConvert()
                saveUser(login)
            } catch (e: Exception) {
                ToastUtils.showShort(e.message)
            }
        }
    }
    suspend fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val login = RetrofitManager.apiServices().login(
                    LoginReqBean(
                        "android", 1, password, username, 1
                    )
                ).dataConvert()
                saveUser(login)
            } catch (e: Exception) {
                ToastUtils.showShort(e.message)
            }
        }
    }
    private fun saveUser(userBean: LoginRespBean) {
        loginRespBean.postValue(userBean)
        SPUtils.getInstance(BaseApp.appContext).apply {
            saveToken(userBean.accessToken)
            saveUser(userBean)
        }
    }
}