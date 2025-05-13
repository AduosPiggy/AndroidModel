package com.example.androidmodel.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.androidmodel.R
import com.example.androidmodel.activities.main.MainActivity
import com.example.androidmodel.activities.main2.Main2Activity
import com.example.androidmodel.activities.test.TestActivity
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityLoginBinding
import com.example.androidmodel.tools.ActivityManager
import com.example.androidmodel.tools.startActivity
import kotlinx.coroutines.launch

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
        ActivityManager.addActivity(this)

        binding.goHome.setOnClickListener {
            startActivity(MainActivity::class)
        }
        binding.btnTest.setOnClickListener{
            startActivity(TestActivity::class)
        }
        binding.btnSignIn.setOnClickListener {
            val name = binding.etUsername.text.toString().trim()
            val pwd = binding.etPwd.text.toString().trim()
            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)){
                lifecycleScope.launch {
                    viewModel.login(name,pwd)
                }
            }else{
                Toast.makeText(this@LoginActivity, "请输入用户名或密码", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.goMain2.setOnClickListener{
            startActivity(Main2Activity::class)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ActivityManager.removeActivity(this)
        //consider exec finishAll ?
        ActivityManager.finishAll()
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }

}