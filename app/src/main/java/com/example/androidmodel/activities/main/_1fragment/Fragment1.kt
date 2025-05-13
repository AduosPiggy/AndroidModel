package com.example.androidmodel.activities.main._1fragment

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMFragment
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.FragmentNav1Binding

/**
 * @author kfflso
 * @data 2025-05-13 20:44
 * @plus:
 */
@ContentLayout(R.layout.fragment_nav_1)
class Fragment1 : BaseVMFragment<Fragment1VM,FragmentNav1Binding>(){
    companion object{
        var isDebug = true
    }

    override fun initView(savedInstanceState: Bundle?){
        super.initView(savedInstanceState)
        binding.txtview.text = "第一个fragment"
    }
}