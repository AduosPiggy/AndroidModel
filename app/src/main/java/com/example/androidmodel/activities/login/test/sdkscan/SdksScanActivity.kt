package com.example.androidmodel.activities.login.test.sdkscan

import android.os.Bundle
import android.util.Log
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivitySdksscanBinding
import com.example.androidmodel.tools.apkinfo.ApkParserUsedSdkUtils

/**
 * @author kfflso
 * @data 2024/9/19 14:05
 * @plus:
 */
@ContentLayout(R.layout.activity_sdksscan)
class SdksScanActivity: BaseVMActivity<SdksScanVM, ActivitySdksscanBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnInstall.setOnClickListener{
            Log.d("twy","btnInstall")
        }
        binding.btnLaunch.setOnClickListener{
            Log.d("twy","btnLaunch")
        }
        binding.btnScanSdks.setOnClickListener{
            val apkParserUsedSdkUtils = ApkParserUsedSdkUtils("/data/local/tmp/weibo.apk","")
            val sdk_id = apkParserUsedSdkUtils.sdkUsed_
            Log.d("twy","btnScanSdks")
        }

    }

    override fun initViews() {

    }

    override fun initDatas() {

    }

}