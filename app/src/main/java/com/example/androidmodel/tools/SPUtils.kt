package com.example.androidmodel.tools

import android.content.Context
import android.content.SharedPreferences
import com.blankj.utilcode.util.GsonUtils
import com.example.androidmodel.base.BaseApp
import com.example.androidmodel.network.responseBeans.LoginRespBean

class SPUtils private constructor(mContext: Context) {

    companion object {
        fun getInstance(context: Context = BaseApp.appContext) = SPUtils(context)
        private const val SP_NAME = "Data"

        private const val KEY_IS_PAD = "key_is_pad"
        private const val KEY_TOKEN = "key_token"
        private const val KEY_USER_INFO = "key_user_info"
    }

    private val prefs: SharedPreferences by lazy {
        mContext.getSharedPreferences(
            SP_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun isPad() = prefs.getBoolean(KEY_IS_PAD, false)
    fun setPad(value: Boolean) = prefs.edit().putBoolean(KEY_IS_PAD, value).apply()
    fun saveToken(value: String) { prefs.edit().putString(KEY_TOKEN, value).apply() }
    fun getToken(): String { return prefs.getString(KEY_TOKEN, "").toString() }
    fun saveUser(value: LoginRespBean) { prefs.edit().putString(KEY_USER_INFO, GsonUtils.toJson(value)).apply() }
    /**
     * 清除
     */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /**
     * 删除某Key的值
     */
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}