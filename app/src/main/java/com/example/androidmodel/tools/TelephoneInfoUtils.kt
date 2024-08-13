package com.example.androidmodel.tools

import android.Manifest
import android.content.Context
import android.telephony.TelephonyManager
import com.example.androidmodel.base.BaseApp
import kotlin.coroutines.coroutineContext

/**
 * @author kfflso
 * @data 2024/8/13/013 11:13
 * @plus:
 */
object TelephoneInfoUtils {
    private val telephonyManager: TelephonyManager = BaseApp.appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val telephonePermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE
    )

    fun requestPhonePermission(){

    }


}