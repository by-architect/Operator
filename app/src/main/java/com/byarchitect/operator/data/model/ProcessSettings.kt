package com.byarchitect.operator.data.model

import com.byarchitect.operator.data.model.ProcessLabel

data class ProcessSettings(
    val refreshRate: Long,
    val processLabels: List<ProcessLabel>
) {

    companion object {
        fun default(): ProcessSettings = ProcessSettings(3000L, listOf(
            ProcessLabel.PID,
            ProcessLabel.NAME,
            ProcessLabel.CPU_PERCENTAGE,
            ProcessLabel.MEM_PERCENTAGE,
))
    }

}