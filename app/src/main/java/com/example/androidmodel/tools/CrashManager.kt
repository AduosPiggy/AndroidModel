//package com.example.androidmodel.tools
//
//import android.content.Context
//import android.os.Build
//import android.provider.Settings
//import com.example.androidmodel.base.BaseApp
//import java.io.File
//import java.io.FileWriter
//import java.io.PrintWriter
//import java.io.StringWriter
//import java.util.*
//
//object CrashManager :
//    Thread.UncaughtExceptionHandler {
//    // 程序的Context对象
//    private val context: Context = BaseApp.appContext
//
//    // 系统默认的UncaughtException处理类
//    private val mDefaultHandler: Thread.UncaughtExceptionHandler =
//        Thread.getDefaultUncaughtExceptionHandler()
//
//    override fun uncaughtException(
//        thread: Thread,
//        throwable: Throwable
//    ) {
//        catchException(context, thread, throwable)
//        mDefaultHandler.uncaughtException(thread, throwable)
//    }
//
//    fun hasErrorReport(): Boolean {
//        val errorLog =
//            context.getSharedPreferences("errorLog", Context.MODE_PRIVATE)
//        val bugReporterFile = errorLog.getString("bugReporterFile", "")
//        return bugReporterFile != null && bugReporterFile.isNotEmpty()
//    }
//
//    val errorFile: File
//        get() {
//            val errorLog =
//                context.getSharedPreferences("errorLog", Context.MODE_PRIVATE)
//            val bugReporterFile = errorLog.getString("bugReporterFile", "")
//            return File(bugReporterFile)
//        }
//
//    val errorFilePath: String?
//        get() {
//            val errorLog =
//                context.getSharedPreferences("errorLog", Context.MODE_PRIVATE)
//            return errorLog.getString("bugReporterFile", "")
//        }
//
//    fun init() {
//        Thread.setDefaultUncaughtExceptionHandler(this)
//    }
//
//
//    private var logFile: File? = null
//    private var logWriter: FileWriter? = null
//    private fun LogG(context: Context, s: String) {
//        try {
//            if (logFile == null) {
//                createWriter(context)
//            }
//            logWriter = FileWriter(logFile, true)
//            logWriter!!.write(
//                """
//    $s
//
//    """.trimIndent()
//            )
//        } catch (e: Exception) {
//        } finally {
//            try {
//                logWriter!!.close()
//            } catch (e: Exception) {
//            }
//        }
//    }
//
//    private fun createWriter(context: Context) {
//        try {
//            logFile = File(
//                context.externalCacheDir,
//                Date(System.currentTimeMillis()).toString() + ".txt"
//            )
//            logWriter = FileWriter(logFile, true)
//            logWriter!!.write(
//                """
//                    BaseApp.Start===============
//                    packageName>>>${context.packageName}
//                    appVer>>>
//                    """.trimIndent() + context.packageManager.getPackageInfo(
//                    context.packageName,
//                    0
//                ).versionName + "(" + context.packageManager
//                    .getPackageInfo(context.packageName, 0).versionCode + ")" +
//                        "\nmanufacturer>>>" + Build.BRAND.toLowerCase() +
//                        "\nmodel>>>" + Build.MODEL.toLowerCase() +
//                        "\nos-ver>>>" + Build.VERSION.RELEASE.toLowerCase() +
//                        "\nandroidId>>>" + Settings.System.getString(
//                    context.contentResolver,
//                    Settings.System.ANDROID_ID
//                ) +
//                        "\n\nLog.Start===============\n"
//            )
//            logWriter!!.close()
//        } catch (e: Exception) {
//        }
//    }
//
//    private fun catchException(
//        context: Context,
//        t: Thread?,
//        e: Throwable
//    ) {
//        if (logFile == null) {
//            createWriter(context)
//        }
//        val sp =
//            context.getSharedPreferences("errorLog", Context.MODE_PRIVATE)
//        sp.edit().putString("bugReporterFile", logFile!!.absolutePath).apply()
//        LogG(
//            context, """
//
//     Error>>>
//     ${getExceptionInfo(e)}
//     """.trimIndent()
//        )
//    }
//
//    private fun getExceptionInfo(e: Throwable): String {
//        val sw = StringWriter()
//        val pw = PrintWriter(sw, true)
//        e.printStackTrace(pw)
//        pw.flush()
//        sw.flush()
//        return sw.toString()
//    }
//}