package com.example.androidmodel.base

import android.app.Application
import android.content.Context
import android.content.pm.VersionedPackage
import android.os.Build
import com.example.androidmodel.tools.Kfflso_SPUtils

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        application = this
//        LitePal.initialize(appContext)

//        RxHttpManager.init(this)
//        CrashManager.init()
    }

    override fun onTerminate() {
        super.onTerminate()

    }

    companion object {
        lateinit var appContext: Context
        lateinit var application: Application

        //handle share preferences
        object SPHelper{
            //share preferences
            fun isPad() = Kfflso_SPUtils.getInstance(appContext).isPad()
        }

        //handle common function
        object CommonHelper{
            fun getVersionCode():Long{
                var res = 0L;
                val pm = application.packageManager
                var packageInfo = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    val vp = VersionedPackage(application.packageName,0)
                    pm.getPackageInfo(vp,0)
                }else{
                    pm.getPackageInfo(application.packageName,0)
                }
                res = packageInfo.longVersionCode
                return res
            }

        }

    }


}