package com.example.androidmodel.activities.login.test.sdkscan

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivitySdksscanBinding
import com.example.androidmodel.tools.ActivityManager
import com.example.androidmodel.tools.sdkscan.SdksScanUtil

/**
 * @author kfflso
 * @data 2024/9/19 14:05
 * @plus:
 *      finished: scan the apk used the third sdks;
 *      to do:modify framework source code as Kfflso_Framework_ActivityThread.java
 */
@ContentLayout(R.layout.activity_sdksscan)
class SdksScanActivity: BaseVMActivity<SdksScanVM, ActivitySdksscanBinding>(){
    private lateinit var kfflsoSdksScanUtil : SdksScanUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.addActivity(this)

        initUtils()
        clickers()

    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }
    private fun initUtils(){
        kfflsoSdksScanUtil = SdksScanUtil(
            this,
            "/data/local/tmp/weibo.apk"
        )
    }

    private fun clickers(){
        binding.btnDownloadAndInstall.setOnClickListener{
            // to do: apk 静默安装, packageManager install method
        }

        binding.btnLaunchAndWork.setOnClickListener{
            kfflsoSdksScanUtil.testTaskFlow();

        }

        binding.btnGetSdkScanResults.setOnClickListener{
            kfflsoSdksScanUtil.testGetResult()
        }


    }



    override fun initViews() {

    }

    override fun initDatas() {

    }

}