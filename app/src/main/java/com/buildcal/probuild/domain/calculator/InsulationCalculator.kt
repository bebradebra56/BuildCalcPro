package com.buildcal.probuild.domain.calculator

import kotlin.math.ceil

data class InsulationResult(
    val rollsNeeded: Int,
    val rollsWithWaste: Int,
    val totalCoverage: Double
)

object InsulationCalculator {
    fun calculate(
        surfaceArea: Double,
        rollWidthM: Double = 0.6,
        rollLengthM: Double = 10.0,
        wasteFactor: Double = 1.10
    ): InsulationResult {
        val rollCoverage = rollWidthM * rollLengthM
        val rollsNeeded = ceil(surfaceArea / rollCoverage).toInt()
        val rollsWithWaste = ceil(surfaceArea * wasteFactor / rollCoverage).toInt()
        val totalCoverage = rollsWithWaste * rollCoverage
        return InsulationResult(
            rollsNeeded = rollsNeeded,
            rollsWithWaste = rollsWithWaste,
            totalCoverage = totalCoverage
        )
    }
}
