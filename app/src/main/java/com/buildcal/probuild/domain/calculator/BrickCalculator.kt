package com.buildcal.probuild.domain.calculator

import kotlin.math.ceil

data class BrickResult(
    val wallArea: Double,
    val bricksNeeded: Int,
    val bricksWithWaste: Int,
    val mortar: Double
)

object BrickCalculator {
    fun calculate(
        wallHeight: Double,
        wallWidth: Double,
        brickLengthMm: Double = 250.0,
        brickHeightMm: Double = 65.0,
        mortarMm: Double = 10.0,
        wasteFactor: Double = 1.05
    ): BrickResult {
        val wallArea = wallHeight * wallWidth
        val effectiveLength = (brickLengthMm + mortarMm) / 1000.0
        val effectiveHeight = (brickHeightMm + mortarMm) / 1000.0
        val brickFaceArea = effectiveLength * effectiveHeight
        val bricksPerM2 = 1.0 / brickFaceArea
        val rawBricks = wallArea * bricksPerM2
        val bricksNeeded = ceil(rawBricks).toInt()
        val bricksWithWaste = ceil(rawBricks * wasteFactor).toInt()
        val mortarVolume = wallArea * 0.075 * 0.3
        return BrickResult(
            wallArea = wallArea,
            bricksNeeded = bricksNeeded,
            bricksWithWaste = bricksWithWaste,
            mortar = mortarVolume
        )
    }
}
