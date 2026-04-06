package com.buildcal.probuild.rgjof.presentation.pushhandler

import android.os.Bundle
import android.util.Log
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication

class BuildCalcProPushHandler {
    fun buildCalcProHandlePush(extras: Bundle?) {
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Extras from Push = ${extras?.keySet()}")
        if (extras != null) {
            val map = buildCalcProBundleToMap(extras)
            Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Map from Push = $map")
            map?.let {
                if (map.containsKey("url")) {
                    BuildCalcProApplication.BUILD_CALC_PRO_FB_LI = map["url"]
                    Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "UrlFromActivity = $map")
                }
            }
        } else {
            Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Push data no!")
        }
    }

    private fun buildCalcProBundleToMap(extras: Bundle): Map<String, String?>? {
        val map: MutableMap<String, String?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras.getString(key)
        }
        return map
    }

}