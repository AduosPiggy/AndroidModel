package com.example.androidmodel.tools


import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.Utils
import com.example.androidmodel.BuildConfig
import com.example.androidmodel.base.BaseApp
import kotlin.reflect.KClass

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(Utils.getApp(), this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(Utils.getApp(), this, duration).show()
}

fun Float.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(Utils.getApp(), this.toString(), duration).show()
}

fun String.log(tag:String) {
    Log.e(tag,this)
}

inline fun <reified T:Activity>Activity.jump(noinline block: (Intent.() -> Unit)? = null){
    val intent = Intent(this, T::class.java).apply {
        block?.invoke(this)
    }
    startActivity(intent)
}

inline fun <reified T:Activity>Fragment.jump(noinline block: (Intent.() -> Unit)? = null){
    val intent = Intent(activity, T::class.java).apply {
        block?.invoke(this)
    }
    startActivity(intent)
}
inline fun <reified T:Activity>Fragment.jumpForResult(code:Int,noinline block: (Intent.() -> Unit)? = null){
    val intent = Intent(activity, T::class.java).apply {
        block?.invoke(this)
    }
    startActivityForResult(intent,code)
}

fun <T : Activity> Activity.startActivity(clazz: KClass<T>, block: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, clazz.java).apply {
        block?.invoke(this)
    }
    startActivity(intent)
}

fun <T : Activity> Fragment.startActivity(clazz: KClass<T>, block: (Intent.() -> Unit)? = null) {
    val intent = Intent(activity, clazz.java).apply {
        block?.invoke(this)
    }
    startActivity(intent)
}

fun <T : Activity> Fragment.startActivityForResult(clazz: KClass<T>, code:Int, block: (Intent.() -> Unit)? = null) {
    val intent = Intent(activity, clazz.java).apply {
        block?.invoke(this)
    }
    startActivityForResult(intent,code)
}

fun <T : Activity> Activity.startActivity(clazz: KClass<T>, code:Int, block: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, clazz.java).apply {
        block?.invoke(this)
    }
    startActivityForResult(intent,code)
}

val showLog: Boolean = BuildConfig.DEBUG
fun String.logD(tag: String) {
    if (showLog) {
        Log.d(tag, this)
    }
}

fun Int.logD(tag: String) {
    if (showLog) {
        Log.d(tag, this.toString())
    }
}

fun Float.logD(tag: String) {
    if (showLog) {
        Log.d(tag, this.toString())
    }
}

fun String.logE(tag: String) {
    if (showLog) {
        Log.e(tag, this)
    }
}

fun Int.logE(tag: String) {
    if (showLog) {
        Log.e(tag, this.toString())
    }
}

fun Float.logE(tag: String) {
    if (showLog) {
        Log.e(tag, this.toString())
    }
}

fun Int.dp2px(): Float {
    return (0.5f + this * Resources.getSystem().displayMetrics.density)
}
//只保留小数点后两位
fun Float.roundToTwoDecimalPlaces(): Float {
    return "%.2f".format(this).toFloat()
}
fun Float.floatToString(): String {
    return this.toString()
}

fun Float.dp2px() : Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        BaseApp.appContext.resources.displayMetrics
    )
}