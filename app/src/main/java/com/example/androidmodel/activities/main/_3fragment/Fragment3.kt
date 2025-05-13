package com.example.androidmodel.activities.main._3fragment

import android.os.Bundle
import android.util.Log
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMFragment
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.FragmentNav3Binding

/**
 * @author kfflso
 * @data 2025-05-13 20:44
 * @plus:
 */
@ContentLayout(R.layout.fragment_nav_3)
class Fragment3 : BaseVMFragment<Fragment3VM,FragmentNav3Binding>(){
    companion object{
        var isDebug = true
    }

    override fun initView(savedInstanceState: Bundle?){
        super.initView(savedInstanceState)
        binding.txtview.text = "第三个fragment"
        binding.txtview.setOnClickListener{
            Log.d("kfflsol","change to fragment 3")
        }
    }
}