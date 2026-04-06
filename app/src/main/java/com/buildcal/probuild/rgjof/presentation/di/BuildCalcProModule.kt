package com.buildcal.probuild.rgjof.presentation.di

import com.buildcal.probuild.rgjof.data.repo.BuildCalcProRepository
import com.buildcal.probuild.rgjof.data.shar.BuildCalcProSharedPreference
import com.buildcal.probuild.rgjof.data.utils.BuildCalcProPushToken
import com.buildcal.probuild.rgjof.data.utils.BuildCalcProSystemService
import com.buildcal.probuild.rgjof.domain.usecases.BuildCalcProGetAllUseCase
import com.buildcal.probuild.rgjof.presentation.pushhandler.BuildCalcProPushHandler
import com.buildcal.probuild.rgjof.presentation.ui.load.BuildCalcProLoadViewModel
import com.buildcal.probuild.rgjof.presentation.ui.view.BuildCalcProViFun
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val buildCalcProModule = module {
    factory {
        BuildCalcProPushHandler()
    }
    single {
        BuildCalcProRepository()
    }
    single {
        BuildCalcProSharedPreference(get())
    }
    factory {
        BuildCalcProPushToken()
    }
    factory {
        BuildCalcProSystemService(get())
    }
    factory {
        BuildCalcProGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        BuildCalcProViFun(get())
    }
    viewModel {
        BuildCalcProLoadViewModel(get(), get(), get())
    }
}