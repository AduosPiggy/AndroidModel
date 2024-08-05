package com.example.androidmodel.base

import android.app.Application
import android.content.Context
import com.example.androidmodel.tools.SPUtils

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
//        LitePal.initialize(appContext)

//        RxHttpManager.init(this)
//        CrashManager.init()
    }

    override fun onTerminate() {
        super.onTerminate()

    }

    companion object {
        lateinit var appContext: Context
        fun isPad() = SPUtils.getInstance(appContext).isPad()

    }

}