package com.example.androidmodel.activities.login.test.broadcast

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityBroadcastSimulateBinding
import com.example.androidmodel.databinding.ActivityTestBinding
import com.example.androidmodel.tools.BroadCastSimulateUtil
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.sdkscan.SdksScanUtils

/**
 * @author kfflso
 * @data 2024/9/23 17:50
 * @plus:
 */
@ContentLayout(R.layout.activity_broadcast_simulate)
class BroadSimulateActivity: BaseVMActivity<BroadSimulateVM,ActivityBroadcastSimulateBinding>() {
    private lateinit var broadcastSimulateUtil: BroadCastSimulateUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)
        initUtils()
        clickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        CustomActivityManager.removeActivity(this)
    }

    private fun initUtils(){
        broadcastSimulateUtil = BroadCastSimulateUtil(this)
    }
    private fun clickers(){
        binding.btnSignalStrengthChange.setOnClickListener{
            broadcastSimulateUtil.simulateSignalStrengthChange(75)
        }
        binding.btnBatteryLevelChange.setOnClickListener{
            broadcastSimulateUtil.simulateBatteryChange(85)
        }
        binding.btnTimeChange.setOnClickListener{
            broadcastSimulateUtil.simulateTimeChange(System.currentTimeMillis())
        }
        binding.btnScreenUnlock.setOnClickListener{
            broadcastSimulateUtil.simulateScreenUnlock()
        }
        binding.btnScreenLock.setOnClickListener{
            broadcastSimulateUtil.simulateScreenLock()
        }
        binding.btnMediaMounted.setOnClickListener{
            broadcastSimulateUtil.simulateMediaMounted()
        }
        binding.btnUSBMode.setOnClickListener{
            broadcastSimulateUtil.simulateUsbModeChange()
        }
        binding.btnUserDial.setOnClickListener{
            broadcastSimulateUtil.simulateUserDial("1234567890")
        }
        binding.btnUserCall.setOnClickListener{
            broadcastSimulateUtil.simulateUserCall("0987654321")
        }
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }
}
