package com.example.androidmodel.activities.login.test.broadcast

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityBroadcastSimulateBinding
import com.example.androidmodel.tools.BroadcastSimulateUtil
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.PermissionUtils

/**
 * @author kfflso
 * @data 2024/9/23 17:50
 * @plus:
 */
@ContentLayout(R.layout.activity_broadcast_simulate)
class BroadSimulateActivity: BaseVMActivity<BroadSimulateVM,ActivityBroadcastSimulateBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)
        preLoad()
        clickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        CustomActivityManager.removeActivity(this)

    }

    private fun preLoad(){

    }

    private fun clickers(){
        clickerGet()
        clickerMock()
    }

    private fun clickerGet(){

    }
    private fun clickerMock(){
        binding.btnRegisterBroadCast.setOnClickListener{

        }

        binding.btnSignalStrength.setOnClickListener{

        }
        binding.btnBatteryLevel.setOnClickListener{

        }
        binding.btnTime.setOnClickListener{
//            //java.lang.SecurityException: Permission Denial: not allowed to send broadcast android.intent.action.SCREEN_OFF from pid=13526, uid=10598
//            val bsu = BroadcastSimulateUtil(this)
//            val time = bsu.mockTime
//            bsu.simulateTimeTick(time)
        }
        binding.btnScreenOn.setOnClickListener{

        }
        binding.btnScreenOff.setOnClickListener{
            val bsu = BroadcastSimulateUtil(this)
            bsu.simulateScreenOff()
        }
        binding.btnMediaMounted.setOnClickListener{

        }
        binding.btnUSBMode.setOnClickListener{

        }
        binding.btnUserDial.setOnClickListener{

        }
        binding.btnUserCall.setOnClickListener{
            val bsu = BroadcastSimulateUtil(this)
            bsu.simulateDial("19102855916")
        }
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }
}
