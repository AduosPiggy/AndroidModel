package com.example.androidmodel.activities.test

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityHomeBinding
import com.example.androidmodel.databinding.ActivityTestBinding
import com.example.androidmodel.tools.PermissionUtils
import com.example.androidmodel.tools.StringUtils
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

        binding.btnApkinfo.setOnClickListener{
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val REQUEST_CODE_PERMISSIONS = 0
            PermissionUtils.checkAndRequestMorePermissions(this, permissions, REQUEST_CODE_PERMISSIONS)
            val apkInfoImpl = ApkInfoImpl(this,"/data/local/tmp/wenjianjieyawang.apk")
            val apkInfoJson = apkInfoImpl.apkInfoJson
            val apkInfo = apkInfoImpl.apkInfo
        }

        binding.btnChineseEquals.setOnClickListener{
            val isEquals = StringUtils.getInstance().equals("资质与规则","视频")
            val isEquals2 = StringUtils.getInstance().equals2("资质与规则","视频")
            Log.d("twy001", isEquals.toString())
            Log.d("twy001", isEquals2.toString())
        }

    }
    override fun initViews() {

    }

    override fun initDatas() {

    }
}