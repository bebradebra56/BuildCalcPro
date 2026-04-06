package com.buildcal.probuild.rgjof.domain.usecases

import android.util.Log
import com.buildcal.probuild.rgjof.data.repo.BuildCalcProRepository
import com.buildcal.probuild.rgjof.data.utils.BuildCalcProPushToken
import com.buildcal.probuild.rgjof.data.utils.BuildCalcProSystemService
import com.buildcal.probuild.rgjof.domain.model.BuildCalcProEntity
import com.buildcal.probuild.rgjof.domain.model.BuildCalcProParam
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication

class BuildCalcProGetAllUseCase(
    private val buildCalcProRepository: BuildCalcProRepository,
    private val buildCalcProSystemService: BuildCalcProSystemService,
    private val buildCalcProPushToken: BuildCalcProPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : BuildCalcProEntity?{
        val params = BuildCalcProParam(
            buildCalcProLocale = buildCalcProSystemService.buildCalcProGetLocale(),
            buildCalcProPushToken = buildCalcProPushToken.buildCalcProGetToken(),
            buildCalcProAfId = buildCalcProSystemService.buildCalcProGetAppsflyerId()
        )
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Params for request: $params")
        return buildCalcProRepository.buildCalcProGetClient(params, conversion)
    }



}