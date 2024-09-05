package com.example.androidmodel.tools

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

/**
 * @author kfflso
 * @data 2024/9/5 9:54
 * @plus:
 */
object GzipUtils {
    /**
     * @return Gzip 压缩 ByteArray; 返回ByteArray;
     */
    fun gzipCompress(data: ByteArray): ByteArray? {
        val byteStream = ByteArrayOutputStream()
        try {
            GZIPOutputStream(byteStream).use { gzipOutputStream ->
                gzipOutputStream.write(
                    data
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return byteStream.toByteArray()
    }
}