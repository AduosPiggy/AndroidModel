package com.example.androidmodel.activities.main2

import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.androidmodel.R
import com.example.androidmodel.activities.main._1fragment.Fragment1
import com.example.androidmodel.activities.main._2fragment.Fragment2
import com.example.androidmodel.activities.main._3fragment.Fragment3
import com.example.androidmodel.activities.main._4fragment.Fragment4
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityMain2Binding
import com.example.androidmodel.tools.ActivityManager

/**
 * @author kfflso
 * @data 2024/8/1/001 18:51
 * @plus:
 */
@ContentLayout(R.layout.activity_main2)
class Main2Activity : BaseVMActivity<Main2VM, ActivityMain2Binding>() {

    private val fragments = arrayOf(
        Fragment1(),
        Fragment2(),
        Fragment3(),
        Fragment4()
    )

    private val navItems by lazy {
        arrayOf(
            binding.nav1,
            binding.nav2,
            binding.nav3,
            binding.nav4
        )
    }

    override fun initViews() {
        // 初始化ViewPager
        binding.viewPager.apply {
            adapter = ViewPagerAdapter()
            registerOnPageChangeCallback(pageChangeCallback)
            offscreenPageLimit = fragments.size // 预加载所有页面
            offscreenPageLimit = 1 //当fragment很多时,只预加载相邻界面
        }

        // 设置导航点击事件
        navItems.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                binding.viewPager.currentItem = index
            }
        }
    }

    override fun initDatas() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.addActivity(this)


    }
    override fun onDestroy() {
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
        ActivityManager.removeActivity(this)
    }

    private inner class ViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateNavState(position)
        }
    }

    private fun updateNavState(selectedPosition: Int) {
        navItems.forEachIndexed { index, textView ->
            textView.isSelected = index == selectedPosition
        }
    }
}