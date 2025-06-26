package com.byarchitect.operator.data.model

data class ProcessSettings(
    val refreshRate: Long,
    val processLabels: List<ProcessLabel>
) {

    companion object {
        fun default(): ProcessSettings = ProcessSettings(1000L, listOf(ProcessLabel.PID, ProcessLabel.NAME))
    }

}