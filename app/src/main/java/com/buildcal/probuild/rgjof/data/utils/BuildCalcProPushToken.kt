package com.buildcal.probuild.rgjof.data.utils

import android.util.Log
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class BuildCalcProPushToken {

    suspend fun buildCalcProGetToken(
        buildCalcProMaxAttempts: Int = 3,
        buildCalcProDelayMs: Long = 1500
    ): String {

        repeat(buildCalcProMaxAttempts - 1) {
            try {
                val buildCalcProToken = FirebaseMessaging.getInstance().token.await()
                return buildCalcProToken
            } catch (e: Exception) {
                Log.e(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Token error (attempt ${it + 1}): ${e.message}")
                delay(buildCalcProDelayMs)
            }
        }

        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Token error final: ${e.message}")
            "null"
        }
    }


}