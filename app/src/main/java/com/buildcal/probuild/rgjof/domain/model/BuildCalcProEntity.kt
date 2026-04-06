package com.buildcal.probuild.rgjof.domain.model

import com.google.gson.annotations.SerializedName


data class BuildCalcProEntity (
    @SerializedName("ok")
    val buildCalcProOk: String,
    @SerializedName("url")
    val buildCalcProUrl: String,
    @SerializedName("expires")
    val buildCalcProExpires: Long,
)