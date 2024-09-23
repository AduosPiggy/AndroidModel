package com.example.androidmodel.activities.login.test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.activities.login.test.sdkscan.SdksScanActivity
import com.example.androidmodel.base.BaseApp
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityTestBinding
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.PermissionUtils
import com.example.androidmodel.tools.apkinfo.ApkInfoImpl

/**
 * @author kfflso
 * @data 2024/9/5 14:39
 * @plus:
 */
@ContentLayout(R.layout.activity_test)
class TestActivity : BaseVMActivity<TestVM,ActivityTestBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)

        binding.btnApkinfo.setOnClickListener{
            val vc = BaseApp.Companion.CommonHelper.getVersionCode()

            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val REQUEST_CODE_PERMISSIONS = 0
            PermissionUtils.checkAndRequestMorePermissions(this, permissions, REQUEST_CODE_PERMISSIONS)
            val apkInfoImpl = ApkInfoImpl(this,"/data/local/tmp/weibo.apk")
            val apkInfoJson = apkInfoImpl.apkInfoJson
            val apkInfo = apkInfoImpl.apkInfo
        }

        binding.btnRunSdksScan.setOnClickListener{

            val intent = Intent(this@TestActivity, SdksScanActivity::class.java)
            startActivity(intent)
        }

        binding.btnMockSystemBroadcast.setOnClickListener{

        }

    }
    override fun initViews() {

    }

    override fun initDatas() {

    }
}