package com.buildcal.probuild.domain.calculator

import kotlin.math.ceil

data class DrywallResult(
    val panelsNeeded: Int,
    val panelsWithWaste: Int,
    val screwsNeeded: Int,
    val jointsCompound: Double
)

object DrywallCalculator {
    fun calculate(
        wallArea: Double,
        panelWidthMm: Double = 1200.0,
        panelHeightMm: Double = 2500.0,
        wasteFactor: Double = 1.10
    ): DrywallResult {
        val panelArea = (panelWidthMm / 1000.0) * (panelHeightMm / 1000.0)
        val panelsNeeded = ceil(wallArea / panelArea).toInt()
        val panelsWithWaste = ceil(wallArea * wasteFactor / panelArea).toInt()
        val screwsNeeded = (panelsWithWaste * 25)
        val jointsCompound = panelsWithWaste * 0.5
        return DrywallResult(
            panelsNeeded = panelsNeeded,
            panelsWithWaste = panelsWithWaste,
            screwsNeeded = screwsNeeded,
            jointsCompound = jointsCompound
        )
    }
}
