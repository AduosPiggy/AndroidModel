package com.example.androidmodel.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.androidmodel.base.annotation.ContentLayout
import org.greenrobot.eventbus.EventBus


abstract class BaseFragment : Fragment() {

    //是否第一次加载
    private var isFirst: Boolean = true

    private var mUseEventBus = false

    private lateinit var rootView: View
    val TAG: String by lazy {
        javaClass.simpleName
    }

    var layoutResId = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayoutWithOutAnnotation()
        initAttributes()
        mUseEventBus = userEventBus()
        rootView = inflater.inflate(layoutResId, container, false)
        return rootView
    }

    open fun initLayoutWithOutAnnotation() {}

    private fun initAttributes() {
        val contentLayout = javaClass.getAnnotation(ContentLayout::class.java)
        if (contentLayout != null) {
            if (contentLayout.value != -1) {
                layoutResId = contentLayout.value
            }
        } else {
            getFragmentLayout()
        }
    }

    private fun getFragmentLayout() = layoutResId

    open fun setFragmentLayout(layoutResId: Int) = layoutResId

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(savedInstanceState)
    }

    open fun initView(savedInstanceState: Bundle?) {}

    open fun userEventBus():Boolean = false

    override fun onResume() {
        super.onResume()
        onVisible()
        if (mUseEventBus) {
            if (EventBus.getDefault().isRegistered(this).not()) {
                EventBus.getDefault().register(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mUseEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    private var isFirstShow = true
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (isFirstShow) {
            isFirstShow = false
        } else {
            isShow(!hidden)
        }
    }

    open fun isShow(show: Boolean) {}

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

}