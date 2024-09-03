package com.example.androidmodel.activities.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.androidmodel.R
import com.example.androidmodel.activities.home.HomeActivity
import com.example.androidmodel.base.BaseApp
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityLoginBinding
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.PermissionUtils
import com.example.androidmodel.tools.apkinfo.ApkInfoImpl

//import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author kfflso
 * @data 2024/8/1/001 18:33
 * @plus:
 */
@ContentLayout(R.layout.activity_login)
class LoginActivity : BaseVMActivity<LoginVM,ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)

        binding.goHome.setOnClickListener {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val vc = BaseApp.Companion.CommonHelper.getVersionCode()
            Log.d("twy001","twy find vc $vc")
        }
        binding.btnSignIn.setOnClickListener {

        }
        binding.btnLogApk.setOnClickListener{
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val REQUEST_CODE_PERMISSIONS = 0
            PermissionUtils.checkAndRequestMorePermissions(this, permissions, REQUEST_CODE_PERMISSIONS)
            val apkInfoImpl = ApkInfoImpl(this,"/data/local/tmp/weibo.apk")
            val apkInfo = apkInfoImpl.apkInfo
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        CustomActivityManager.removeActivity(this)
        //考虑是否执行finishAll
        CustomActivityManager.finishAll()
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }

}