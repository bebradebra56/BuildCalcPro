package com.buildcal.probuild.rgjof.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication
import java.util.Locale

class BuildCalcProSystemService(private val context: Context) {

    fun buildCalcProGetAppsflyerId(): String {
        val appsflyrid = AppsFlyerLib.getInstance().getAppsFlyerUID(context) ?: ""
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "AppsFlyer: AppsFlyer Id = $appsflyrid")
        return appsflyrid
    }

    fun buildCalcProGetLocale() : String {
        return  Locale.getDefault().language
    }

    fun buildCalcProIsOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                return true
            }
        }
        return false
    }

}