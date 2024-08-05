package com.example.androidmodel.tools

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * @author kfflso
 * @data 2024/8/5/005 11:41
 * @plus:
 *
 */


//    fragmentSwitchNavBar = Builder()
//        .fragmentManager(getSupportFragmentManager())
//        .containerId(R.id.cl_container)
//        .clickableViews(cl_home, cl_ac, cl_dc, cl_setting, cl_strategy) // 需要切换的点击切换的fragment
//        .fragments(homeFragment, acFragment, dcFragment, settingFragment, strategyFragment)//new fragment
//        .onClickListener(null) //View.OnClickListener changeTag
//        .build()
//    //default show view: home
//    fragmentSwitchNavBar.changeTag(cl_home)
class FragmentSwitcher : View.OnClickListener {

    interface OnClickListener {
        fun onClickChangeBG(view: View)
    }

    interface OnDoubleClickListener {
        fun onDoubleClick(view: View)
    }

    private lateinit var fragmentManager: FragmentManager
    private var containerId: Int = 0
    private lateinit var clickableViews: List<View>
    private lateinit var fragments: Array<Fragment>
    private var onClickListener: OnClickListener? = null
    private var onDoubleClickListener: OnDoubleClickListener? = null

    private var currentFragment: Fragment? = null
    private var currentSelectedView: View? = null

    fun setClickableViews(clickableViews: Array<View>) {
        this.clickableViews = clickableViews.asList()
        for (view in clickableViews) {
            view.setOnClickListener(this)
        }
    }

    fun setClickableViewsAndListener(onClickListener: OnClickListener, clickableViews: Array<View>) {
        this.onClickListener = onClickListener
        setClickableViews(clickableViews)
    }

    fun setFragments(fragments: Array<Fragment>) {
        this.fragments = fragments
    }

    fun getFragmentByView(targetView: View): Fragment {
        val targetPosition = clickableViews.indexOf(targetView)
        return fragments[targetPosition]
    }

    fun changeTag(targetView: View) {
        if (targetView == currentSelectedView) return

        val fragmentTransaction = fragmentManager.beginTransaction()
        var targetFragment = fragmentManager.findFragmentByTag(targetView.id.toString())
        val targetPosition = clickableViews.indexOf(targetView)

        if (targetFragment == null) {
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment!!)
                currentSelectedView?.isSelected = false
            }
            targetFragment = fragments[targetPosition]
            fragmentTransaction.add(containerId, targetFragment, targetView.id.toString())
        } else {
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment!!)
                currentSelectedView?.isSelected = false
            }
            fragmentTransaction.show(targetFragment)
        }
        fragmentTransaction.commit()
        currentFragment = targetFragment
        currentSelectedView = targetView
        currentSelectedView?.isSelected = true
    }

    override fun onClick(v: View) {
        changeTag(v)
    }

    class Builder {
        private var fragmentManager: FragmentManager? = null
        private var containerId: Int = 0
        private var clickableViews: List<View>? = null
        private var fragments: Array<Fragment>? = null
        private var onClickListener: OnClickListener? = null
        private var onDoubleClickListener: OnDoubleClickListener? = null

        fun fragmentManager(fragmentManager: FragmentManager) = apply {
            this.fragmentManager = fragmentManager
        }

        fun containerId(containerId: Int) = apply {
            this.containerId = containerId
        }

        fun clickableViews(clickableViews: Array<View>) = apply {
            this.clickableViews = clickableViews.asList()
        }

        fun fragments(fragments: Array<Fragment>) = apply {
            this.fragments = fragments
        }

        fun onClickListener(onClickListener: OnClickListener) = apply {
            this.onClickListener = onClickListener
        }

        fun onDoubleClickListener(onDoubleClickListener: OnDoubleClickListener) = apply {
            this.onDoubleClickListener = onDoubleClickListener
        }

        fun build(): FragmentSwitcher {
            val fragmentSwitcher = FragmentSwitcher()
            fragmentSwitcher.fragmentManager = this.fragmentManager!!
            fragmentSwitcher.containerId = this.containerId
            fragmentSwitcher.clickableViews = this.clickableViews!!
            fragmentSwitcher.fragments = this.fragments!!
            fragmentSwitcher.onClickListener = this.onClickListener
            fragmentSwitcher.onDoubleClickListener = this.onDoubleClickListener

//            if (fragmentSwitcher.onDoubleClickListener == null) {
//                for (view in fragmentSwitcher.clickableViews) {
//                    view.setOnClickListener {
//                        fragmentSwitcher.changeTag(view)
//                        fragmentSwitcher.onClickListener?.onClickChangeBG(view)
//                    }
//                }
//            } else {
//                for (view in fragmentSwitcher.clickableViews) {
//                    val gestureDetector = GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
//                        override fun onSingleTapUp(e: MotionEvent): Boolean {
//                            fragmentSwitcher.changeTag(view)
//                            fragmentSwitcher.onClickListener?.onClickChangeBG(view)
//                            return true
//                        }
//
//                        override fun onDown(e: MotionEvent): Boolean {
//                            return true
//                        }
//
//                        override fun onDoubleTap(e: MotionEvent): Boolean {
//                            fragmentSwitcher.onDoubleClickListener?.onDoubleClick(view)
//                            return true
//                        }
//                    })
//
//                    view.setOnTouchListener { _, event ->
//                        gestureDetector.onTouchEvent(event)
//                    }
//                }
//            }
            return fragmentSwitcher
        }
    }
}
