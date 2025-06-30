package com.byarchitect.operator.common.util.system

import com.byarchitect.operator.common.model.math.RootResult
import com.byarchitect.operator.common.util.math.Calculator

object SystemAnalyzer {


    fun findCpuThreshold(cpuHistory: List<Double>): RootResult {
        val systemResponseFunction = { cpu: Double ->
            if (cpu >= 100.0) Double.MAX_VALUE
            else 1.0 / (100.0 - cpu) - 2.0
        }

        if (cpuHistory.isEmpty()) {
            return Calculator.bisectionMethod(systemResponseFunction, 50.0, 99.0)
        }

        val maxCpu = cpuHistory.maxOrNull() ?: 99.0
        val highBound = maxCpu.coerceIn(60.0, 99.0)
        val lowBound = (highBound - 20).coerceAtLeast(0.0)

        return Calculator.bisectionMethod(systemResponseFunction, lowBound, highBound)
    }


}