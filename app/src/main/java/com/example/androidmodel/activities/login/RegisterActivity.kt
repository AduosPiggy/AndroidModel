package com.example.androidmodel.activities.login

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.androidmodel.R
import com.example.androidmodel.activities.main.MainActivity
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityRegisterBinding
import com.example.androidmodel.tools.ActivityManager
import com.example.androidmodel.tools.startActivity
import kotlinx.coroutines.launch

/**
 * @author kfflso
 * @data 2025-05-07 15:48
 * @plus:
 */
@ContentLayout(R.layout.activity_register)
class RegisterActivity : BaseVMActivity<LoginVM,ActivityRegisterBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }

    override fun initViews() {
        binding.SignIn.setOnClickListener{
            var phoneNum = binding.etPhoneNum.text.toString().trim()
            var password =binding.PasswordAgain.text.toString().trim()
            if(!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(password)){
                lifecycleScope.launch {
                    viewModel.register(phoneNum,password)
                }
            }else{
                Toast.makeText(this@RegisterActivity,"为完善信息,注册失败",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun initDatas() {
        viewModel.loginRespBean.observe(this){
            Toast.makeText(this@RegisterActivity,"验证通过,注册成功",Toast.LENGTH_SHORT).show()
            finish()
            startActivity(MainActivity::class)
        }
    }

}