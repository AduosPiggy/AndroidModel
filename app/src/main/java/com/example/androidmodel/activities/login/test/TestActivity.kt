package com.example.androidmodel.activities.login.test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.androidmodel.R
import com.example.androidmodel.activities.login.test.broadcast.BroadSimulateActivity
import com.example.androidmodel.activities.login.test.sdkscan.SdksScanActivity
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityTestBinding
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.logs.LogsUtils
import com.example.androidmodel.tools.PermissionUtils
import com.example.androidmodel.tools.apkinfo.ApkInfoImpl

/**
 * @author kfflso
 * @data 2024/9/5 14:39
 * @plus:
 */
@ContentLayout(R.layout.activity_test)
class TestActivity : BaseVMActivity<TestVM,ActivityTestBinding>() {
    var tag = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)

        binding.btnReqPermission.setOnClickListener{
//            val vc = BaseApp.Companion.CommonHelper.getVersionCode()
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val reqCode = 0
            PermissionUtils.checkAndRequestMorePermissions(this, permissions, reqCode)
            Log.d(TAG,"get permissions: READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE")
        }

        binding.btnApkinfo.setOnClickListener{

            val time1 = System.currentTimeMillis()
            Log.d(TAG,"pre: $time1")
            val apkInfoImpl = ApkInfoImpl(this,"/data/local/tmp/qiangtandenglu3d.apk")
            val apkInfoJson = apkInfoImpl.apkInfoJson
//            val apkInfo = apkInfoImpl.apkInfo
            val time2 = System.currentTimeMillis()
            Log.d(TAG,"after: $time2")
            Log.d(TAG,"time gap: ${(time2-time1)/1000} s")

        }

        binding.btnRunSdksScan.setOnClickListener{

            val intent = Intent(this@TestActivity, SdksScanActivity::class.java)
            startActivity(intent)
        }

        binding.btnMockSystemBroadcast.setOnClickListener{
            val intent = Intent(this@TestActivity, BroadSimulateActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogsUtils.setOnClickListener{
            LogsUtils.initLogDir(this)
            LogsUtils.logToFileAsync("LogsUtils","hello twy");
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        CustomActivityManager.removeActivity(this)
    }
    override fun initViews() {

    }

    override fun initDatas() {

    }
}