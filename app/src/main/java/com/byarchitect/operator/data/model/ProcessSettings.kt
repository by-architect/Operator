package com.byarchitect.operator.data.model

data class ProcessSettings(
    val refreshRate: Long,
) {

    val refreshRateAsSeconds: Int
        get() = (refreshRate / 1000).toInt()

    companion object {
        fun default(): ProcessSettings {
            return ProcessSettings(3000L)
        }

    }
}