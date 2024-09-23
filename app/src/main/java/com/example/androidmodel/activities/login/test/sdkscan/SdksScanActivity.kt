package com.example.androidmodel.activities.login.test.sdkscan

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivitySdksscanBinding
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.sdkscan.SdksScanUtil
import com.example.androidmodel.tools.sdkscan.SdksScanUtils

/**
 * @author kfflso
 * @data 2024/9/19 14:05
 * @plus:
 */
@ContentLayout(R.layout.activity_sdksscan)
class SdksScanActivity: BaseVMActivity<SdksScanVM, ActivitySdksscanBinding>(){
    private lateinit var sdksScanUtils: SdksScanUtils
    private lateinit var sdksScanUtil: SdksScanUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)

        initUtils()
        clickers()

    }
    override fun onDestroy() {
        super.onDestroy()
        CustomActivityManager.removeActivity(this)
    }
    private fun initUtils(){
        sdksScanUtils = SdksScanUtils(this, "/data/local/tmp/weibo.apk")
        sdksScanUtil = SdksScanUtil(this,"/data/local/tmp/weibo.apk")
    }

    private fun clickers(){
        binding.btnInstall.setOnClickListener{
            // to do: apk 静默安装, packageManager install method
        }

        binding.btnLaunch.setOnClickListener{
            val packageName = sdksScanUtils.packageName
            sdksScanUtils.launchTargetApp(packageName)

        }

        binding.btnSaveClassNames.setOnClickListener{
            sdksScanUtils.saveLaunchAppClassNames()

        }

        binding.btnScanSdks.setOnClickListener{
            sdksScanUtil.sdks

        }

    }



    override fun initViews() {

    }

    override fun initDatas() {

    }

}