package com.example.androidmodel.tools

import android.content.Context
import android.content.SharedPreferences
import com.example.androidmodel.base.BaseApp

class SPUtils private constructor(mContext: Context) {

    companion object {
        fun getInstance(context: Context = BaseApp.appContext) = SPUtils(context)
        private const val SP_NAME = "Data"

        private const val KEY_IS_PAD = "key_is_pad"
    }

    private val prefs: SharedPreferences by lazy {
        mContext.getSharedPreferences(
            SP_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun isPad() = prefs.getBoolean(KEY_IS_PAD, false)
    fun setPad(value: Boolean) = prefs.edit().putBoolean(KEY_IS_PAD, value).apply()

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