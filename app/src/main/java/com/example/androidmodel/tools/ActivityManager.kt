package com.example.androidmodel.tools

import android.app.Activity

/**
 * @author kfflso
 * @data 2024/8/5/005 11:13
 * @plus:
 */
object ActivityManager {
    private val activities = mutableListOf<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
        activity.finish()
    }

    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }
}
