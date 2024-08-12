package com.example.androidmodel.tools

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * @author kfflso
 * @data 2024/8/12/012 15:03
 * @plus: UDP 的发送和接受消息
 *
 * val beanSendCfg = Bean_Udp("192.168.0.zzz",10012,"kfflso",3) //udp发送到的ip port msg times
 * var beanReceiveCfg = BeanReceiveCfg(9000,"receivedMsg") // udp 接收msg的 port
 * UDP.receiveMsg()
 * UDP.processMsg() // 这个方法自定义实现; beanReceiveCfg 中的 receivedMsg 也应根据业务来 处理
 * UDP.sendMsg()
 * UDP.stop()
 */

//udp send info by follow bean
//ip, port; msg: send this msg; sendTimes;
data class BeanSendCfg(val ip: String, val port: Int, val msg: String, var sendTimes: Int) {
    init {
        if (sendTimes <= 0) {
            sendTimes = 1
        }
    }
}
//udp receive msg by follow port
data class BeanReceiveCfg(
    val port: Int,
    var receivedMsg: String
)

object UDP {
    private final val TAG = javaClass.simpleName
    //default udp send info bean
    var beanSendCfg = BeanSendCfg("192.168.1.zzz",8000,"sendMsg",2)
    //default udp receive info bean
    var beanReceiveCfg = BeanReceiveCfg(9000,"receivedMsg")

    private var socket: DatagramSocket? = null
    private var thread_receiveMsg: Thread? = null
    private var thread_sendMsg: Thread? = null
    private val threadList: MutableList<Thread> = mutableListOf()

    init {
        InitThreads()
    }
    // start received udp msg
    fun receiveMsg(){
        if(thread_receiveMsg?.isAlive != true){
            thread_receiveMsg?.start()
        }
    }
    // start send msg by udp
    fun sendMsg() {
        if(thread_sendMsg?.isAlive != true){
            thread_sendMsg?.start()
        }
    }
    // stop all tasks
    fun stop(){
        stopAndClearThreads()
        thread_receiveMsg?.let {
            if (it.isAlive){
                it.interrupt()
            }
            thread_receiveMsg = null
        }
        thread_sendMsg?.let {
            if (it.isAlive){
                it.interrupt()
            }
            thread_sendMsg = null
        }
        socket?.close()
    }
    private fun InitThreads(){
        thread_receiveMsg = Thread {
            try {
                val socket = DatagramSocket(beanSendCfg.port)
                val buffer = ByteArray(1024)
                while (!Thread.currentThread().isInterrupted) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)
                    beanReceiveCfg.receivedMsg = String(packet.data, 0, packet.length)
                    Log.d(TAG, "port: ${beanSendCfg.port} received msg: ${beanReceiveCfg.receivedMsg}")
                    processMsg(beanReceiveCfg.receivedMsg)
                }
            } catch (e: IOException) {
                if (!Thread.currentThread().isInterrupted) {
                    e.printStackTrace()
                }
            }
        }
        thread_sendMsg = Thread {
            try {
                Log.d(TAG, "socket info; ip: ${beanSendCfg.ip}; port: ${beanSendCfg.port}; sendTimes: ${beanSendCfg.sendTimes}; \n" + " sendMsg: ${beanSendCfg.msg};")
                val localhost = InetAddress.getByName(beanSendCfg.ip)
                val data = beanSendCfg.msg.toByteArray()
                val packet = DatagramPacket(data, data.size, localhost, beanSendCfg.port)
                for (i in 0 until beanSendCfg.sendTimes) {
                    if (Thread.currentThread().isInterrupted) {
                        break
                    }
                    socket?.send(packet)
                    Thread.sleep(50)
                }
            } catch (e: IOException) {
                if (!Thread.currentThread().isInterrupted) {
                    e.printStackTrace()
                }
            }
        }
    }
    // 处理接收到的Msg receivedMsg全局变量,注意内存泄露
    fun processMsg(receivedMsg: String){
        if (receivedMsg.isEmpty()) {
            Log.d(TAG, "udp received msg isEmpty")
            return
        }
        val thread_processMsg = Thread {
            try {
                when (receivedMsg) {
                    "hello" -> {
                        Log.d(TAG, "udp received msg: $receivedMsg")
                    }
                    "kfflso" -> {
                        Log.d(TAG, "udp received msg: $receivedMsg")
                    }
                    else -> {
                        Log.d(TAG, "udp received msg: $receivedMsg")
                    }
                }
            } finally {
                removeThread(Thread.currentThread())
            }
        }
        thread_processMsg.start()
        addThread(thread_processMsg)
    }
    private fun addThread(thread: Thread) {
        synchronized(threadList) {
            threadList.add(thread)
        }
    }
    private fun removeThread(thread: Thread) {
        synchronized(threadList) {
            threadList.remove(thread)
        }
    }
    private fun stopAndClearThreads() {
        synchronized(threadList) {
            if (threadList.isNotEmpty()){
                threadList.forEach { thread ->
                    if (thread.isAlive) {
                        thread.interrupt()
                    }
                }
                threadList.clear()
            }
        }
    }

}