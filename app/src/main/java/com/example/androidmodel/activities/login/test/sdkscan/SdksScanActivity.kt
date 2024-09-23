package com.example.androidmodel.activities.login.test.sdkscan

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.os.Bundle
import android.util.Log
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivitySdksscanBinding
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.sdkscan.SdksScanUtils

/**
 * @author kfflso
 * @data 2024/9/19 14:05
 * @plus:
 */
@ContentLayout(R.layout.activity_sdksscan)
class SdksScanActivity: BaseVMActivity<SdksScanVM, ActivitySdksscanBinding>(){
    private lateinit var sdksScanUtils: SdksScanUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)

        initUtils()
        clickers()

    }
    private fun initUtils(){
        sdksScanUtils = SdksScanUtils(this, "/data/local/tmp/weibo.apk")
    }

    private fun clickers(){
        binding.btnInstall.setOnClickListener{

        }

        binding.btnLaunch.setOnClickListener{
            val packageName = sdksScanUtils.packageName
            sdksScanUtils.launchTargetApp(packageName)

//            val sdks = sdksScanUtils.sdks
//            Log.d("twy","sdks: ${Gson().toJson(sdks)}")
        }

        binding.btnScanSdks.setOnClickListener{
//            val dexFileList = sdksScanUtils.dexFiles
//            Log.d("twy","dexFileList: ${Gson().toJson(dexFileList)}")
            val save = sdksScanUtils.saveLaunchAppClassNames()

        }

    }



    override fun initViews() {

    }

    override fun initDatas() {

    }

}