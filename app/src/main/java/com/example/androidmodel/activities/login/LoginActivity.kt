package com.example.androidmodel.activities.login

import android.content.Intent
import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.activities.home.HomeActivity
import com.example.androidmodel.activities.login.test.TestActivity
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityLoginBinding
import com.example.androidmodel.tools.CustomActivityManager

/**
 * @author kfflso
 * @data 2024/8/1/001 18:33
 * @plus:
 *      使用binding:
 *      1.注意 activity_login.xml中最外层 layout布局;
 *      2.注意绑定了 tools:context=".activities.login.LoginActivity"
 *      3.注意绑定了 ActivityLoginBinding
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

        }
        binding.btnSignIn.setOnClickListener {

        }
        binding.btnTest.setOnClickListener{
            val intent = Intent(this@LoginActivity, TestActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        CustomActivityManager.removeActivity(this)
        //consider exec finishAll ?
        CustomActivityManager.finishAll()
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }

}