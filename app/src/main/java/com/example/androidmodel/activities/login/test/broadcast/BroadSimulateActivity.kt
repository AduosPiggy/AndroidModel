package com.example.androidmodel.activities.login.test.broadcast

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityBroadcastSimulateBinding
import com.example.androidmodel.tools.Kfflso_ActivityManager
import com.example.androidmodel.tools.Kfflso_Simulation
import com.example.androidmodel.tools.permission.Kfflso_PermissionImpl
import com.example.androidmodel.tools.screen.ScreenControl

/**
 * @author kfflso
 * @data 2024/9/23 17:50
 * @plus:
 */
@ContentLayout(R.layout.activity_broadcast_simulate)
class BroadSimulateActivity: BaseVMActivity<BroadSimulateVM,ActivityBroadcastSimulateBinding>() {

    private lateinit var simulation: Kfflso_Simulation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Kfflso_ActivityManager.addActivity(this)
        simulation = Kfflso_Simulation()
        clickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        Kfflso_ActivityManager.removeActivity(this)

    }
    private fun clickers(){
        binding.btnSignalWifi.setOnClickListener{
            simulation.simulateSignalStrengthWifi(simulation.signal_strength_wifi)
        }

        binding.btnSignalMobile.setOnClickListener{
            simulation.simulateSignalStrengthMobile(simulation.signal_strength_mobile)
        }
        binding.btnBattery.setOnClickListener{
            simulation.simulateBatteryChanged(this,simulation.battery_level)
        }
        binding.btnSystemTime.setOnClickListener{
            simulation.simulateSystemTime(simulation.system_time)
        }
        binding.btnScreenLock.setOnClickListener{
            simulation.simulateScreenLock()
            Thread.sleep(3000)
            simulation.simulateScreenUnLock()

        }
        binding.btnScreenUnlock.setOnClickListener{
            simulation.simulateScreenLock()
            Thread.sleep(3000)
            simulation.simulateScreenUnLock()

        }
        binding.btnMediaMounted.setOnClickListener{
            simulation.simulateMediaMounted(this,simulation.file_media_mounted)
        }
        binding.btnUsb.setOnClickListener{
            simulation.simulateUsbDeviceAttached(this)
        }
        binding.btnCall.setOnClickListener{
            simulation.simulateUserCall(this,simulation.phone_number_call)
        }
        binding.btnDial.setOnClickListener{
            simulation.simulateDial(this,simulation.phone_number_dial)
        }
    }


    override fun initViews() {

    }

    override fun initDatas() {

    }
}
