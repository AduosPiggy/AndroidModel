package com.example.androidmodel.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.blankj.utilcode.util.Utils


open class BaseViewModel : AndroidViewModel(Utils.getApp()), LifecycleObserver {

}