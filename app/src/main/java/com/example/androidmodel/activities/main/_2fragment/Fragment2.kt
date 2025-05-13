package com.example.androidmodel.activities.main._2fragment

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMFragment
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.FragmentNav2Binding

/**
 * @author kfflso
 * @data 2025-05-13 20:44
 * @plus:
 */
@ContentLayout(R.layout.fragment_nav_2)
class Fragment2 : BaseVMFragment<Fragment2VM,FragmentNav2Binding>(){
    companion object{
        var isDebug = true
    }

    override fun initView(savedInstanceState: Bundle?){
        super.initView(savedInstanceState)
        binding.txtview.text = "第二个fragment"
    }
}