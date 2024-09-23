package com.example.androidmodel.activities.login

import androidx.lifecycle.MutableLiveData
import com.example.androidmodel.base.BaseViewModel
import com.example.androidmodel.beans.LoginRespBean

/**
 * @author kfflso
 * @data 2024/8/1/001 18:34
 * @plus:
 */
class LoginVM : BaseViewModel() {
    var loginRespBean = MutableLiveData<LoginRespBean>()

//    suspend fun login(username: String, password: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val login = RetrofitManager.apiServices().login(
//                    LoginReqBean(
//                        "android", 1, password, username, 1
//                    )
//                ).dataConvert()
//                saveUser(login)
//            } catch (e: Exception) {
//                ToastUtils.showShort(e.message)
//            }
//        }
//    }
}