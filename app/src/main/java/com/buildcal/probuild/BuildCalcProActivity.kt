package com.buildcal.probuild

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.buildcal.probuild.rgjof.BuildCalcProGlobalLayoutUtil
import com.buildcal.probuild.rgjof.buildCalcProSetupSystemBars
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication
import com.buildcal.probuild.rgjof.presentation.pushhandler.BuildCalcProPushHandler
import org.koin.android.ext.android.inject

class BuildCalcProActivity : AppCompatActivity() {

    private val buildCalcProPushHandler by inject<BuildCalcProPushHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildCalcProSetupSystemBars()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_build_calc_pro)

        val buildCalcProRootView = findViewById<View>(android.R.id.content)
        BuildCalcProGlobalLayoutUtil().buildCalcProAssistActivity(this)
        ViewCompat.setOnApplyWindowInsetsListener(buildCalcProRootView) { buildCalcProView, buildCalcProInsets ->
            val buildCalcProSystemBars = buildCalcProInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val buildCalcProDisplayCutout = buildCalcProInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val buildCalcProIme = buildCalcProInsets.getInsets(WindowInsetsCompat.Type.ime())


            val buildCalcProTopPadding = maxOf(buildCalcProSystemBars.top, buildCalcProDisplayCutout.top)
            val buildCalcProLeftPadding = maxOf(buildCalcProSystemBars.left, buildCalcProDisplayCutout.left)
            val buildCalcProRightPadding = maxOf(buildCalcProSystemBars.right, buildCalcProDisplayCutout.right)
            window.setSoftInputMode(BuildCalcProApplication.buildCalcProInputMode)

            if (window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "ADJUST PUN")
                val buildCalcProBottomInset = maxOf(buildCalcProSystemBars.bottom, buildCalcProDisplayCutout.bottom)

                buildCalcProView.setPadding(buildCalcProLeftPadding, buildCalcProTopPadding, buildCalcProRightPadding, 0)

                buildCalcProView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = buildCalcProBottomInset
                }
            } else {
                Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "ADJUST RESIZE")

                val buildCalcProBottomInset = maxOf(buildCalcProSystemBars.bottom, buildCalcProDisplayCutout.bottom, buildCalcProIme.bottom)

                buildCalcProView.setPadding(buildCalcProLeftPadding, buildCalcProTopPadding, buildCalcProRightPadding, 0)

                buildCalcProView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = buildCalcProBottomInset
                }
            }



            WindowInsetsCompat.CONSUMED
        }
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Activity onCreate()")
        buildCalcProPushHandler.buildCalcProHandlePush(intent.extras)
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            buildCalcProSetupSystemBars()
        }
    }

    override fun onResume() {
        super.onResume()
        buildCalcProSetupSystemBars()
    }
}