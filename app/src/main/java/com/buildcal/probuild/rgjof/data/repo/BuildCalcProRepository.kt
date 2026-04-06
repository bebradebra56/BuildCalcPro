package com.buildcal.probuild.rgjof.data.repo

import android.util.Log
import com.buildcal.probuild.rgjof.domain.model.BuildCalcProEntity
import com.buildcal.probuild.rgjof.domain.model.BuildCalcProParam
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication.Companion.BUILD_CALC_PRO_MAIN_TAG
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BuildCalcProApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun buildCalcProGetClient(
        @Body jsonString: JsonObject,
    ): Call<BuildCalcProEntity>
}


private const val BUILD_CALC_PRO_MAIN = "https://buiildcalcpro.com/"
class BuildCalcProRepository {

    suspend fun buildCalcProGetClient(
        buildCalcProParam: BuildCalcProParam,
        buildCalcProConversion: MutableMap<String, Any>?
    ): BuildCalcProEntity? {
        val gson = Gson()
        val api = buildCalcProGetApi(BUILD_CALC_PRO_MAIN, null)

        val buildCalcProJsonObject = gson.toJsonTree(buildCalcProParam).asJsonObject
        buildCalcProConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            buildCalcProJsonObject.add(key, element)
        }
        return try {
            val buildCalcProRequest: Call<BuildCalcProEntity> = api.buildCalcProGetClient(
                jsonString = buildCalcProJsonObject,
            )
            val buildCalcProResult = buildCalcProRequest.awaitResponse()
            Log.d(BUILD_CALC_PRO_MAIN_TAG, "Retrofit: Result code: ${buildCalcProResult.code()}")
            if (buildCalcProResult.code() == 200) {
                Log.d(BUILD_CALC_PRO_MAIN_TAG, "Retrofit: Get request success")
                Log.d(BUILD_CALC_PRO_MAIN_TAG, "Retrofit: Code = ${buildCalcProResult.code()}")
                Log.d(BUILD_CALC_PRO_MAIN_TAG, "Retrofit: ${buildCalcProResult.body()}")
                buildCalcProResult.body()
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d(BUILD_CALC_PRO_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(BUILD_CALC_PRO_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun buildCalcProGetApi(url: String, client: OkHttpClient?) : BuildCalcProApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
