package com.buildcal.probuild.domain.calculator

import kotlin.math.ceil

data class TileResult(
    val tilesNeeded: Int,
    val tilesWithWaste: Int,
    val groutKg: Double,
    val adhesiveKg: Double
)

object TileCalculator {
    fun calculate(
        floorArea: Double,
        tileWidthMm: Double = 300.0,
        tileHeightMm: Double = 300.0,
        groutWidthMm: Double = 3.0,
        wasteFactor: Double = 1.10
    ): TileResult {
        val tileArea = ((tileWidthMm + groutWidthMm) * (tileHeightMm + groutWidthMm)) / 1_000_000.0
        val tilesNeeded = ceil(floorArea / tileArea).toInt()
        val tilesWithWaste = ceil(floorArea * wasteFactor / tileArea).toInt()
        val groutKg = floorArea * 1.5
        val adhesiveKg = floorArea * 5.0
        return TileResult(
            tilesNeeded = tilesNeeded,
            tilesWithWaste = tilesWithWaste,
            groutKg = groutKg,
            adhesiveKg = adhesiveKg
        )
    }
}
