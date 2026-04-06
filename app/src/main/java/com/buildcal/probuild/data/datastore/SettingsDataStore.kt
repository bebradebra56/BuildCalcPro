package com.buildcal.probuild.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val UNITS = stringPreferencesKey("units")
        val CURRENCY = stringPreferencesKey("currency")
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        val PROFILE_NAME = stringPreferencesKey("profile_name")
        val PROFILE_EMAIL = stringPreferencesKey("profile_email")
        val WASTE_FACTOR = floatPreferencesKey("waste_factor")
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[ONBOARDING_COMPLETED] ?: false }

    val units: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[UNITS] ?: "metric" }

    val currency: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[CURRENCY] ?: "USD" }

    val currencySymbol: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[CURRENCY_SYMBOL] ?: "$" }

    val profileName: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[PROFILE_NAME] ?: "" }

    val profileEmail: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[PROFILE_EMAIL] ?: "" }

    val wasteFactor: Flow<Float> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[WASTE_FACTOR] ?: 1.10f }

    suspend fun setOnboardingCompleted(value: Boolean) {
        context.dataStore.edit { it[ONBOARDING_COMPLETED] = value }
    }

    suspend fun setUnits(value: String) {
        context.dataStore.edit { it[UNITS] = value }
    }

    suspend fun setCurrency(currency: String, symbol: String) {
        context.dataStore.edit {
            it[CURRENCY] = currency
            it[CURRENCY_SYMBOL] = symbol
        }
    }

    suspend fun setProfileName(value: String) {
        context.dataStore.edit { it[PROFILE_NAME] = value }
    }

    suspend fun setProfileEmail(value: String) {
        context.dataStore.edit { it[PROFILE_EMAIL] = value }
    }

    suspend fun setWasteFactor(value: Float) {
        context.dataStore.edit { it[WASTE_FACTOR] = value }
    }
}
