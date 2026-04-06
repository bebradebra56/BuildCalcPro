package com.buildcal.probuild.rgjof.presentation.ui.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel

class BuildCalcProDataStore : ViewModel(){
    val buildCalcProViList: MutableList<BuildCalcProVi> = mutableListOf()
    var buildCalcProIsFirstCreate = true
    @SuppressLint("StaticFieldLeak")
    lateinit var buildCalcProContainerView: FrameLayout
    @SuppressLint("StaticFieldLeak")
    lateinit var buildCalcProView: BuildCalcProVi

}