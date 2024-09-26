package com.example.androidmodel.activities.login.test.broadcast

import android.os.Bundle
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityBroadcastSimulateBinding
import com.example.androidmodel.tools.CustomActivityManager

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
        binding.btnGetPhoneSignalStrength.setOnClickListener{

        }
        binding.btnGetPhoneBatteryLevel.setOnClickListener{

        }
        binding.btnGetPhoneTime.setOnClickListener{

        }
    }
    private fun clickerMock(){
        binding.btnRegisterBroadCast.setOnClickListener{

        }

        binding.btnSignalStrength.setOnClickListener{

        }
        binding.btnBatteryLevel.setOnClickListener{

        }
        binding.btnTime.setOnClickListener{

        }
        binding.btnScreenOn.setOnClickListener{

        }
        binding.btnScreenOff.setOnClickListener{

        }
        binding.btnMediaMounted.setOnClickListener{

        }
        binding.btnUSBMode.setOnClickListener{

        }
        binding.btnUserDial.setOnClickListener{

        }
        binding.btnUserCall.setOnClickListener{

        }
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }
}
