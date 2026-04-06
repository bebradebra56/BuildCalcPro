package com.buildcal.probuild.domain.calculator

import kotlin.math.ceil

data class ConcreteResult(
    val volume: Double,
    val cementBags: Int,
    val sandKg: Double,
    val aggregateKg: Double,
    val waterLiters: Double
)

object ConcreteCalculator {
    fun calculate(
        length: Double,
        width: Double,
        height: Double,
        wasteFactor: Double = 1.05
    ): ConcreteResult {
        val volume = length * width * height * wasteFactor
        val cementKg = volume * 320
        val cementBags = ceil(cementKg / 50.0).toInt()
        val sandKg = volume * 600
        val aggregateKg = volume * 1200
        val waterLiters = volume * 180
        return ConcreteResult(
            volume = volume,
            cementBags = cementBags,
            sandKg = sandKg,
            aggregateKg = aggregateKg,
            waterLiters = waterLiters
        )
    }
}
