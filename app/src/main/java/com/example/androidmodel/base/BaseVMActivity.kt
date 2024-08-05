package com.example.androidmodel.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.androidmodel.R
import java.lang.reflect.ParameterizedType

abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewDataBinding> : BaseActivity<VB>() {
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        createViewModel()
        super.onCreate(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val clazz = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(clazz) as VM
        }
    }
}
