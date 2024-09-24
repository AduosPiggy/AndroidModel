package com.example.androidmodel.activities.login.test.broadcast

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityBroadcastSimulateBinding
import com.example.androidmodel.tools.broadcast.BroadCastSendUtil
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.broadcast.BroadCastReceiverUtil

/**
 * @author kfflso
 * @data 2024/9/23 17:50
 * @plus:
 */
@ContentLayout(R.layout.activity_broadcast_simulate)
class BroadSimulateActivity: BaseVMActivity<BroadSimulateVM,ActivityBroadcastSimulateBinding>() {
    private lateinit var broadcastSendUtil: BroadCastSendUtil
    private lateinit var broadCastReceiverUtil: BroadCastReceiverUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)
        initUtils()
        verifyPermissions()
        clickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        CustomActivityManager.removeActivity(this)
        broadCastReceiverUtil.unregisterReceiver()
    }

    private fun initUtils(){
        broadcastSendUtil = BroadCastSendUtil(this)
        broadCastReceiverUtil = BroadCastReceiverUtil(this);
    }
    private fun verifyPermissions(){
        broadcastSendUtil.verifyPermission(this)
    }
    private fun clickers(){
        binding.btnRegisterBroadCast.setOnClickListener{
            broadCastReceiverUtil.registerReceivers()
        }

        binding.btnSignalStrengthChange.setOnClickListener{
            broadcastSendUtil.simulateSignalStrengthChange(75)
        }
        binding.btnBatteryLevelChange.setOnClickListener{
            broadcastSendUtil.simulateBatteryChange(85)
        }
        binding.btnTimeChange.setOnClickListener{
            broadcastSendUtil.simulateTimeChange(System.currentTimeMillis())
        }
        binding.btnScreenUnlock.setOnClickListener{
            broadcastSendUtil.simulateScreenUnlock()
        }
        binding.btnScreenLock.setOnClickListener{
            broadcastSendUtil.simulateScreenLock()
        }
        binding.btnMediaMounted.setOnClickListener{
            broadcastSendUtil.simulateMediaMounted()
        }
        binding.btnUSBMode.setOnClickListener{
            broadcastSendUtil.simulateUsbModeChange()
        }
        binding.btnUserDial.setOnClickListener{
            broadcastSendUtil.simulateUserDial("1234567890")
        }
        binding.btnUserCall.setOnClickListener{
            broadcastSendUtil.simulateUserCall("0987654321")
        }
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }
}
