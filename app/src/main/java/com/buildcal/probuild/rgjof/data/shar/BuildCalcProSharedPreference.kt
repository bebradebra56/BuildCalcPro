package com.buildcal.probuild.rgjof.data.shar

import android.content.Context
import androidx.core.content.edit

class BuildCalcProSharedPreference(context: Context) {
    private val buildCalcProPrefs = context.getSharedPreferences("buildCalcProSharedPrefsAb", Context.MODE_PRIVATE)

    var buildCalcProSavedUrl: String
        get() = buildCalcProPrefs.getString(BUILD_CALC_PRO_SAVED_URL, "") ?: ""
        set(value) = buildCalcProPrefs.edit { putString(BUILD_CALC_PRO_SAVED_URL, value) }

    var buildCalcProExpired : Long
        get() = buildCalcProPrefs.getLong(BUILD_CALC_PRO_EXPIRED, 0L)
        set(value) = buildCalcProPrefs.edit { putLong(BUILD_CALC_PRO_EXPIRED, value) }

    var buildCalcProAppState: Int
        get() = buildCalcProPrefs.getInt(BUILD_CALC_PRO_APPLICATION_STATE, 0)
        set(value) = buildCalcProPrefs.edit { putInt(BUILD_CALC_PRO_APPLICATION_STATE, value) }

    var buildCalcProNotificationRequest: Long
        get() = buildCalcProPrefs.getLong(BUILD_CALC_PRO_NOTIFICAITON_REQUEST, 0L)
        set(value) = buildCalcProPrefs.edit { putLong(BUILD_CALC_PRO_NOTIFICAITON_REQUEST, value) }


    var buildCalcProNotificationState:Int
        get() = buildCalcProPrefs.getInt(BUILD_CALC_PRO_NOTIFICATION_STATE, 0)
        set(value) = buildCalcProPrefs.edit { putInt(BUILD_CALC_PRO_NOTIFICATION_STATE, value) }

    companion object {
        private const val BUILD_CALC_PRO_NOTIFICATION_STATE = "buildCalcProNotificationState"
        private const val BUILD_CALC_PRO_SAVED_URL = "buildCalcProSavedUrl"
        private const val BUILD_CALC_PRO_EXPIRED = "buildCalcProExpired"
        private const val BUILD_CALC_PRO_APPLICATION_STATE = "buildCalcProApplicationState"
        private const val BUILD_CALC_PRO_NOTIFICAITON_REQUEST = "buildCalcProNotificationRequest"
    }
}