package com.buildcal.probuild.rgjof

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication

class BuildCalcProGlobalLayoutUtil {

    private var buildCalcProMChildOfContent: View? = null
    private var buildCalcProUsableHeightPrevious = 0

    fun buildCalcProAssistActivity(activity: Activity) {
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        buildCalcProMChildOfContent = content.getChildAt(0)

        buildCalcProMChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {
        val buildCalcProUsableHeightNow = buildCalcProComputeUsableHeight()
        if (buildCalcProUsableHeightNow != buildCalcProUsableHeightPrevious) {
            val buildCalcProUsableHeightSansKeyboard = buildCalcProMChildOfContent?.rootView?.height ?: 0
            val buildCalcProHeightDifference = buildCalcProUsableHeightSansKeyboard - buildCalcProUsableHeightNow

            if (buildCalcProHeightDifference > (buildCalcProUsableHeightSansKeyboard / 4)) {
                activity.window.setSoftInputMode(BuildCalcProApplication.buildCalcProInputMode)
            } else {
                activity.window.setSoftInputMode(BuildCalcProApplication.buildCalcProInputMode)
            }
//            mChildOfContent?.requestLayout()
            buildCalcProUsableHeightPrevious = buildCalcProUsableHeightNow
        }
    }

    private fun buildCalcProComputeUsableHeight(): Int {
        val r = Rect()
        buildCalcProMChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top  // Visible height без status bar
    }
}