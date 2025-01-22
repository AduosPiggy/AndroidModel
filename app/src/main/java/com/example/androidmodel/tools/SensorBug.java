package com.example.androidmodel.tools;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorBug {
    private Activity activity;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private SensorEventListener sensorEventListener;

    public SensorBug(Activity activity) {
        this.activity = activity;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()){
                    case Sensor.TYPE_ACCELEROMETER:
                        float ax = event.values[0];
                        float ay = event.values[1];
                        float az = event.values[2];
                        Log.d("SensorData", "Accelerometer: x="+ax+", y="+ay+", z="+az);
                    case Sensor.TYPE_GYROSCOPE:
                        float gx = event.values[0];
                        float gy = event.values[1];
                        float gz = event.values[2];
                        Log.d("SensorData", "Accelerometer: x="+gx+", y="+gy+", z="+gz);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    public void start(){
        try {
            Log.d("SensorData", "start_setOnClickListener");
            sensorManager.registerListener(sensorEventListener, accelerometer, 5000); // 5ms -> 200 Hz
            Thread.sleep(2, 500000);// 睡眠 2.5 毫秒
            sensorManager.registerListener(sensorEventListener, gyroscope, 5000); // 5ms -> 200 Hz
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void stop(){
        Log.d("SensorData", "end_setOnClickListener");
        sensorManager.unregisterListener(sensorEventListener);
    }

}
