package com.example.androidmodel.base

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.example.androidmodel.R


class HeaderView(context: Context, attributeSet: AttributeSet) :
    RelativeLayout(context, attributeSet) {
    private var mTvTitle: TextView
    private var mRightLayout: FrameLayout

    init {
        val inflate = View.inflate(context, R.layout.view_header, this)
        inflate.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }
        mTvTitle = inflate.findViewById(R.id.tvTitle)
        mRightLayout = inflate.findViewById(R.id.rightLayout)
    }

    fun setTitle(title: String) {
        mTvTitle.text = title
    }

    fun setRightLaytou(view: View) {
        mRightLayout.addView(view)
    }

}