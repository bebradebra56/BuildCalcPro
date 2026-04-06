package com.buildcal.probuild.di

import com.buildcal.probuild.data.datastore.SettingsDataStore
import com.buildcal.probuild.data.db.AppDatabase
import com.buildcal.probuild.data.repository.*
import com.buildcal.probuild.ui.screens.calculators.CalculatorsViewModel
import com.buildcal.probuild.ui.screens.calendar.CalendarViewModel
import com.buildcal.probuild.ui.screens.dashboard.DashboardViewModel
import com.buildcal.probuild.ui.screens.estimates.EstimatesViewModel
import com.buildcal.probuild.ui.screens.materials.MaterialsViewModel
import com.buildcal.probuild.ui.screens.measurements.MeasurementsViewModel
import com.buildcal.probuild.ui.screens.profile.ProfileViewModel
import com.buildcal.probuild.ui.screens.projects.ProjectsViewModel
import com.buildcal.probuild.ui.screens.reports.ReportsViewModel
import com.buildcal.probuild.ui.screens.rooms.RoomsViewModel
import com.buildcal.probuild.ui.screens.settings.SettingsViewModel
import com.buildcal.probuild.ui.screens.shopping.ShoppingViewModel
import com.buildcal.probuild.ui.screens.tasks.TasksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { AppDatabase.getInstance(androidContext()) }
    single { SettingsDataStore(androidContext()) }

    single { get<AppDatabase>().projectDao() }
    single { get<AppDatabase>().roomDao() }
    single { get<AppDatabase>().materialDao() }
    single { get<AppDatabase>().shoppingDao() }
    single { get<AppDatabase>().measurementDao() }
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().calculationDao() }

    single { ProjectRepository(get()) }
    single { RoomRepository(get()) }
    single { MaterialRepository(get()) }
    single { ShoppingRepository(get()) }
    single { MeasurementRepository(get()) }
    single { TaskRepository(get()) }
    single { CalculationRepository(get()) }

    viewModel { DashboardViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProjectsViewModel(get(), get()) }
    viewModel { (projectId: Long) -> RoomsViewModel(get(), projectId) }
    viewModel { CalculatorsViewModel(get()) }
    viewModel { MaterialsViewModel(get()) }
    viewModel { EstimatesViewModel(get(), get()) }
    viewModel { ShoppingViewModel(get()) }
    viewModel { MeasurementsViewModel(get()) }
    viewModel { ReportsViewModel(get(), get(), get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { TasksViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
