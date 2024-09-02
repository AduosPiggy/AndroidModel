package com.example.androidmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.androidmodel.base.annotation.ContentLayout

/**
 * plus: this one is useless, please check by AndroidManifest.xml launcher
 */
@ContentLayout(R.layout.activity_main)
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val iv = findViewById<ImageView>(R.id.iv_bitmap)
    }

}