package com.byarchitect.operator.common.util.system

import com.byarchitect.operator.common.model.math.RootResult
import com.byarchitect.operator.common.util.math.Calculator

class SystemAnalyzer {

    fun findCpuThreshold(cpuHistory: List<Double>): RootResult {
        // Define function: response_time = f(cpu_usage) - acceptable_response_time = 0
        val systemResponseFunction = { cpu: Double ->
            // Model: response_time = 1 / (100 - cpu_usage) - 2.0 (2 seconds acceptable)
            if (cpu >= 100.0) Double.MAX_VALUE
            else 1.0 / (100.0 - cpu) - 2.0
        }

        return Calculator.bisectionMethod(systemResponseFunction, 50.0, 99.0)
    }

}