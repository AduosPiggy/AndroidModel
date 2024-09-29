package com.example.androidmodel.activities.login.test.sdkscan

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivitySdksscanBinding
import com.example.androidmodel.tools.Kfflso_ActivityManager
import com.example.androidmodel.tools.sdkscan.Kfflso_SdksScanUtil
import com.example.androidmodel.tools.sdkscan.SdksScanUtils

/**
 * @author kfflso
 * @data 2024/9/19 14:05
 * @plus:
 */
@ContentLayout(R.layout.activity_sdksscan)
class SdksScanActivity: BaseVMActivity<SdksScanVM, ActivitySdksscanBinding>(){
    private lateinit var sdksScanUtils: SdksScanUtils
    private lateinit var kfflsoSdksScanUtil: Kfflso_SdksScanUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Kfflso_ActivityManager.addActivity(this)

        initUtils()
        clickers()

    }
    override fun onDestroy() {
        super.onDestroy()
        Kfflso_ActivityManager.removeActivity(this)
    }
    private fun initUtils(){
        sdksScanUtils = SdksScanUtils(this, "/data/local/tmp/weibo.apk")
        kfflsoSdksScanUtil =
            Kfflso_SdksScanUtil(
                this,
                "/data/local/tmp/weibo.apk"
            )
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


        }

    }



    override fun initViews() {

    }

    override fun initDatas() {

    }

}