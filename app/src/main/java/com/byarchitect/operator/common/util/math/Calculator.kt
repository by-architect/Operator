package com.byarchitect.operator.common.util.math

import com.byarchitect.operator.common.model.math.IterativeMethod
import com.byarchitect.operator.common.model.math.RootResult
import kotlin.math.abs

object Calculator {
    fun bisectionMethod(
        f: (Double) -> Double,
        a: Double,
        b: Double,
        tolerance: Double = 1e-10,
        maxIterations: Int = 1000
    ): RootResult {
        var left = a
        var right = b
        var iterations = 0
        var previousMidpoint = Double.MAX_VALUE

        if (f(left) * f(right) > 0) {
            return RootResult(IterativeMethod.BISECTION, Double.NaN, 0, 0.0, Double.MAX_VALUE, false)
        }

        while (iterations < maxIterations) {
            val midpoint = (left + right) / 2
            val fMid = f(midpoint)

            if (abs(fMid) < tolerance || abs(right - left) < tolerance) {
                val convergenceRate = if (iterations > 1) {
                    abs(midpoint - previousMidpoint) / abs(previousMidpoint - ((left + right) / 4))
                } else 0.5

                return RootResult(IterativeMethod.BISECTION, midpoint, iterations, convergenceRate, abs(fMid), true)
            }

            if (f(left) * fMid < 0) {
                right = midpoint
            } else {
                left = midpoint
            }

            previousMidpoint = midpoint
            iterations++
        }

        return RootResult(IterativeMethod.BISECTION, (left + right) / 2, iterations, 0.5, abs(f((left + right) / 2)), false)
    }

}
