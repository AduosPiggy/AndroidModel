package com.example.androidmodel.tools.mock;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;



/**
 * @author kfflso
 * @data 2024/9/26 14:09
 * @plus:
 */
public class MockUtils {
    private Context context;
    private TelephonyManager telephonyManager;

    public MockUtils(Context context) {
        this.context = context;
        initData(context);
    }

    private void initData(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private String getPhoneSignalStrength(Activity activity) {
       String dbm = "";

       return dbm;
    }
}
