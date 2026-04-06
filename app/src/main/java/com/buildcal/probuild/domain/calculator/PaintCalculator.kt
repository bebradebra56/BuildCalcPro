package com.buildcal.probuild.domain.calculator

import kotlin.math.ceil

data class PaintResult(
    val litersNeeded: Double,
    val litersWithWaste: Double,
    val cansNeeded: Int
)

object PaintCalculator {
    fun calculate(
        wallArea: Double,
        coveragePerLiter: Double = 10.0,
        coats: Int = 2,
        canSizeLiters: Double = 4.0,
        wasteFactor: Double = 1.10
    ): PaintResult {
        val litersNeeded = (wallArea / coveragePerLiter) * coats
        val litersWithWaste = litersNeeded * wasteFactor
        val cansNeeded = ceil(litersWithWaste / canSizeLiters).toInt()
        return PaintResult(
            litersNeeded = litersNeeded,
            litersWithWaste = litersWithWaste,
            cansNeeded = cansNeeded
        )
    }
}
