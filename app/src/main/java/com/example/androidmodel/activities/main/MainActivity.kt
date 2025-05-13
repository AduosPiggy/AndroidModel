package com.example.androidmodel.activities.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidmodel.R
import com.example.androidmodel.activities.main._1fragment.Fragment1
import com.example.androidmodel.activities.main._2fragment.Fragment2
import com.example.androidmodel.activities.main._3fragment.Fragment3
import com.example.androidmodel.activities.main._4fragment.Fragment4
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityMainBinding
import com.example.androidmodel.tools.ActivityManager
import com.example.androidmodel.tools.FragmentSwitcher

/**
 * @author kfflso
 * @data 2024/8/1/001 18:51
 * @plus:
 */
@ContentLayout(R.layout.activity_main)
class MainActivity : BaseVMActivity<MainVM,ActivityMainBinding>() {

    private lateinit var fragmentSwitcher: FragmentSwitcher
    private val fragments: Array<Fragment> = arrayOf(
        Fragment1(),
        Fragment2(),
        Fragment3(),
        Fragment4()
    )
    private val clickableViews: Array<View> by lazy {
        arrayOf(
            binding.mainCl1,
            binding.mainCl2,
            binding.mainCl3,
            binding.mainCl4
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.addActivity(this)
        binding.mainCl1.setOnClickListener{
            fragmentSwitcher.changeTag(binding.mainCl1)
        }
        binding.mainCl2.setOnClickListener{
            fragmentSwitcher.changeTag(binding.mainCl2)
        }
        binding.mainCl3.setOnClickListener{
            fragmentSwitcher.changeTag(binding.mainCl3)
        }
        binding.mainCl4.setOnClickListener{
            fragmentSwitcher.changeTag(binding.mainCl4)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }



    override fun initViews() {
        fragmentSwitcher = FragmentSwitcher.Builder()
            .fragmentManager(supportFragmentManager)
            .containerId(R.id.home_container)
            .clickableViews(clickableViews)
            .fragments(fragments)
            .build()
        fragmentSwitcher.changeTag(binding.mainCl1)

    }

    override fun initDatas() {

    }


}