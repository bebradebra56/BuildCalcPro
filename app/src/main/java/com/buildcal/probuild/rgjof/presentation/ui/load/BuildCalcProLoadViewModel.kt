package com.buildcal.probuild.rgjof.presentation.ui.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.rgjof.data.shar.BuildCalcProSharedPreference
import com.buildcal.probuild.rgjof.data.utils.BuildCalcProSystemService
import com.buildcal.probuild.rgjof.domain.usecases.BuildCalcProGetAllUseCase
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProAppsFlyerState
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BuildCalcProLoadViewModel(
    private val buildCalcProGetAllUseCase: BuildCalcProGetAllUseCase,
    private val buildCalcProSharedPreference: BuildCalcProSharedPreference,
    private val buildCalcProSystemService: BuildCalcProSystemService
) : ViewModel() {

    private val _buildCalcProHomeScreenState: MutableStateFlow<BuildCalcProHomeScreenState> =
        MutableStateFlow(BuildCalcProHomeScreenState.BuildCalcProLoading)
    val buildCalcProHomeScreenState = _buildCalcProHomeScreenState.asStateFlow()

    private var buildCalcProGetApps = false


    init {
        viewModelScope.launch {
            when (buildCalcProSharedPreference.buildCalcProAppState) {
                0 -> {
                    if (buildCalcProSystemService.buildCalcProIsOnline()) {
                        BuildCalcProApplication.buildCalcProConversionFlow.collect {
                            when(it) {
                                BuildCalcProAppsFlyerState.BuildCalcProDefault -> {}
                                BuildCalcProAppsFlyerState.BuildCalcProError -> {
                                    buildCalcProSharedPreference.buildCalcProAppState = 2
                                    _buildCalcProHomeScreenState.value =
                                        BuildCalcProHomeScreenState.BuildCalcProError
                                    buildCalcProGetApps = true
                                }
                                is BuildCalcProAppsFlyerState.BuildCalcProSuccess -> {
                                    if (!buildCalcProGetApps) {
                                        buildCalcProGetData(it.buildCalcProData)
                                        buildCalcProGetApps = true
                                    }
                                }
                            }
                        }
                    } else {
                        _buildCalcProHomeScreenState.value =
                            BuildCalcProHomeScreenState.BuildCalcProNotInternet
                    }
                }
                1 -> {
                    if (buildCalcProSystemService.buildCalcProIsOnline()) {
                        if (BuildCalcProApplication.BUILD_CALC_PRO_FB_LI != null) {
                            _buildCalcProHomeScreenState.value =
                                BuildCalcProHomeScreenState.BuildCalcProSuccess(
                                    BuildCalcProApplication.BUILD_CALC_PRO_FB_LI.toString()
                                )
                        } else if (System.currentTimeMillis() / 1000 > buildCalcProSharedPreference.buildCalcProExpired) {
                            Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Current time more then expired, repeat request")
                            BuildCalcProApplication.buildCalcProConversionFlow.collect {
                                when(it) {
                                    BuildCalcProAppsFlyerState.BuildCalcProDefault -> {}
                                    BuildCalcProAppsFlyerState.BuildCalcProError -> {
                                        _buildCalcProHomeScreenState.value =
                                            BuildCalcProHomeScreenState.BuildCalcProSuccess(
                                                buildCalcProSharedPreference.buildCalcProSavedUrl
                                            )
                                        buildCalcProGetApps = true
                                    }
                                    is BuildCalcProAppsFlyerState.BuildCalcProSuccess -> {
                                        if (!buildCalcProGetApps) {
                                            buildCalcProGetData(it.buildCalcProData)
                                            buildCalcProGetApps = true
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Current time less then expired, use saved url")
                            _buildCalcProHomeScreenState.value =
                                BuildCalcProHomeScreenState.BuildCalcProSuccess(
                                    buildCalcProSharedPreference.buildCalcProSavedUrl
                                )
                        }
                    } else {
                        _buildCalcProHomeScreenState.value =
                            BuildCalcProHomeScreenState.BuildCalcProNotInternet
                    }
                }
                2 -> {
                    _buildCalcProHomeScreenState.value =
                        BuildCalcProHomeScreenState.BuildCalcProError
                }
            }
        }
    }


    private suspend fun buildCalcProGetData(conversation: MutableMap<String, Any>?) {
        val buildCalcProData = buildCalcProGetAllUseCase.invoke(conversation)
        if (buildCalcProSharedPreference.buildCalcProAppState == 0) {
            if (buildCalcProData == null) {
                buildCalcProSharedPreference.buildCalcProAppState = 2
                _buildCalcProHomeScreenState.value =
                    BuildCalcProHomeScreenState.BuildCalcProError
            } else {
                buildCalcProSharedPreference.buildCalcProAppState = 1
                buildCalcProSharedPreference.apply {
                    buildCalcProExpired = buildCalcProData.buildCalcProExpires
                    buildCalcProSavedUrl = buildCalcProData.buildCalcProUrl
                }
                _buildCalcProHomeScreenState.value =
                    BuildCalcProHomeScreenState.BuildCalcProSuccess(buildCalcProData.buildCalcProUrl)
            }
        } else  {
            if (buildCalcProData == null) {
                _buildCalcProHomeScreenState.value =
                    BuildCalcProHomeScreenState.BuildCalcProSuccess(
                        buildCalcProSharedPreference.buildCalcProSavedUrl
                    )
            } else {
                buildCalcProSharedPreference.apply {
                    buildCalcProExpired = buildCalcProData.buildCalcProExpires
                    buildCalcProSavedUrl = buildCalcProData.buildCalcProUrl
                }
                _buildCalcProHomeScreenState.value =
                    BuildCalcProHomeScreenState.BuildCalcProSuccess(buildCalcProData.buildCalcProUrl)
            }
        }
    }


    sealed class BuildCalcProHomeScreenState {
        data object BuildCalcProLoading : BuildCalcProHomeScreenState()
        data object BuildCalcProError : BuildCalcProHomeScreenState()
        data class BuildCalcProSuccess(val data: String) : BuildCalcProHomeScreenState()
        data object BuildCalcProNotInternet: BuildCalcProHomeScreenState()
    }
}