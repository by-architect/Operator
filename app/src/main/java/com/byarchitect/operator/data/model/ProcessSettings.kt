package com.byarchitect.operator.data.model

data class ProcessSettings(
    val refreshRate: Long,
) {

    companion object {
        fun default(): ProcessSettings {
            return ProcessSettings(3000L)
        }

    }
}