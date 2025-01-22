package com.example.androidmodel.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

object BitmapUtil {
    // string -> bitmap  base64
    fun stringToBitmap(string: String?): Bitmap? {
        return if (string != null) {
            val bytes = Base64.decode(string, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else {
            null
        }
    }

    // bitmap -> string  base64
    fun bitmapToString(bitmap: Bitmap?): String {
        return if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bytes = stream.toByteArray() // 转为 byte 数组
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } else {
            ""
        }
    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    //读 path的bitmap,返回bitmap的string
    fun imageToBase64(path: String?): String? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        var inputStream: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            inputStream = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(inputStream.available())
            //写入数组
            inputStream.read(data)
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }


}

