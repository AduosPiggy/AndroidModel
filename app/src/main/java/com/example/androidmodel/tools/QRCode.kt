package com.example.androidmodel.tools



/**
 * @author kfflso
 * @data 2024/8/1/001 17:17
 * @plus:
 *      不能中文
 */
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.json.JSONObject



object QRCode {
//    不能中文
//    val imageView = findViewById<ImageView>(R.id.iv_bitmap)
//    val bitmapStr = QRCode.generateJsonStringForQRCode("zjy","ba jie gege")
//    val bitmap = QRCode.generateQRCode(bitmapStr,300,300)
//    imageView.setImageBitmap(bitmap)

//    val encoder = BarcodeEncoder()
//    val bitmap = encoder.encodeBitmap("123", BarcodeFormat.QR_CODE, 300,300)
//    imageView.setImageBitmap(bitmap)


    fun generateJsonStringForQRCode(name: String, type: String): String {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", name)
            jsonObject.put("type", type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }

    fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
            EncodeHintType.MARGIN to 1,
//            EncodeHintType.CHARACTER_SET to "UTF-8"
        )
        return try {
            val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}