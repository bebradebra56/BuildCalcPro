package com.buildcal.probuild.rgjof.domain.model

import com.google.gson.annotations.SerializedName


private const val BUILD_CALC_PRO_A = "com.buildcal.probuild"
private const val BUILD_CALC_PRO_B = "buildcalcpro"
data class BuildCalcProParam (
    @SerializedName("af_id")
    val buildCalcProAfId: String,
    @SerializedName("bundle_id")
    val buildCalcProBundleId: String = BUILD_CALC_PRO_A,
    @SerializedName("os")
    val buildCalcProOs: String = "Android",
    @SerializedName("store_id")
    val buildCalcProStoreId: String = BUILD_CALC_PRO_A,
    @SerializedName("locale")
    val buildCalcProLocale: String,
    @SerializedName("push_token")
    val buildCalcProPushToken: String,
    @SerializedName("firebase_project_id")
    val buildCalcProFirebaseProjectId: String = BUILD_CALC_PRO_B,

    )