package com.example.androidmodel.activities.home

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityHomeBinding
import kotlin.math.log

/**
 * @author kfflso
 * @data 2024/8/1/001 18:51
 * @plus:
 */
@ContentLayout(R.layout.activity_home)
class HomeActivity : BaseVMActivity<HomeVM,ActivityHomeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.homeNavBar.setOnClickListener{
            finish()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }
}