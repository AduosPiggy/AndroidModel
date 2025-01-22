package com.example.androidmodel.activities.home

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityHomeBinding
import com.example.androidmodel.tools.ActivityManager

/**
 * @author kfflso
 * @data 2024/8/1/001 18:51
 * @plus:
 */
@ContentLayout(R.layout.activity_home)
class HomeActivity : BaseVMActivity<HomeVM,ActivityHomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.addActivity(this)

        binding.homeNavBar.setOnClickListener{
            finish()

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }
}