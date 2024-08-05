package com.example.androidmodel.activities.login

import android.content.Intent
import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.activities.home.HomeActivity
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityLoginBinding
import com.example.androidmodel.tools.CustomActivityManager

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

        }
        binding.btnSignIn.setOnClickListener {

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