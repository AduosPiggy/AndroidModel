package com.example.androidmodel.base

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import com.example.androidmodel.R
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.base.annotation.FullScreen
import com.example.androidmodel.base.annotation.Header

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    val TAG: String by lazy {
        javaClass.simpleName
    }
    var isFullScreen = false
    var layoutResId = 1
    lateinit var headerView: HeaderView
    lateinit var title: String
    var hasTitleView = false

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (isImmersionBar()) {
//            setStatusBar()
//        }
        initAttributes()
        _binding = DataBindingUtil.setContentView(this,layoutResId)
        binding.lifecycleOwner = this
        initContentLayout()
        initViews()
        initDatas()
    }
    private fun initAttributes() {
        try {
            val fullScreen = javaClass.getAnnotation(FullScreen::class.java)
            val contentLayout = javaClass.getAnnotation(ContentLayout::class.java)
            val headerLayout = javaClass.getAnnotation(Header::class.java)

            if (fullScreen != null) {
                isFullScreen = fullScreen.value
                if (isFullScreen) {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    );
                }
            }

            if (contentLayout != null) {
                if (contentLayout.value != -1) {
                    layoutResId = contentLayout.value
                }
            }

            if (headerLayout != null) {
                title = headerLayout.title
                hasTitleView = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    protected open fun initContentLayout() {
        if (hasTitleView) {
            val headerContentLayout =
                layoutInflater.inflate(R.layout.content_layout_header, null, false) as LinearLayout
            headerView = headerContentLayout.findViewById(R.id.header)
            headerView.setTitle(title)

//            val contentLayout = layoutInflater.inflate(layoutResId, null, false)
//            headerContentLayout.addView(contentLayout)
//            setContentView(headerContentLayout)

            headerContentLayout.addView(binding.root)
            setContentView(headerContentLayout)
        } else {
            setContentView(binding.root)
        }
    }

    abstract fun initViews()
    abstract fun initDatas()


    open fun isImmersionBar(): Boolean {
        return true
    }

    //设置沉浸式状态栏
    private fun setStatusBar() {
        // 透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

}