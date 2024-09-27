package com.example.androidmodel.activities.login.test.broadcast

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.androidmodel.R
import com.example.androidmodel.base.BaseVMActivity
import com.example.androidmodel.base.annotation.ContentLayout
import com.example.androidmodel.databinding.ActivityBroadcastSimulateBinding
import com.example.androidmodel.tools.BroadcastSimulateUtil
import com.example.androidmodel.tools.CustomActivityManager
import com.example.androidmodel.tools.permission.PermissionImpl
import com.example.androidmodel.tools.screen.ScreenControl

/**
 * @author kfflso
 * @data 2024/9/23 17:50
 * @plus:
 */
@ContentLayout(R.layout.activity_broadcast_simulate)
class BroadSimulateActivity: BaseVMActivity<BroadSimulateVM,ActivityBroadcastSimulateBinding>() {

    private lateinit var screenCtrl: ScreenControl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomActivityManager.addActivity(this)
        initUtils()
        clickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        CustomActivityManager.removeActivity(this)
        screenCtrl.destroy()
    }

    private fun initUtils(){
        screenCtrl = ScreenControl(this)
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
            /* 1 权限申请
             * 2 设备管理器lock
             * 3 5s后点击电源键(无密码设备 屏幕解锁)
             */
            PermissionImpl.verify_screenOnAndOff(this)
            screenCtrl.turnOffScreen()
            Thread.sleep(3000)
            screenCtrl.turnOnScreen()

        }
        binding.btnScreenOff.setOnClickListener{
            PermissionImpl.verify_screenOnAndOff(this)
            screenCtrl.turnOffScreen()

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
