package com.byarchitect.operator.data.repository

import com.byarchitect.operator.data.model.ProcessLabel

class ProcessSettingsRepository {
     fun getRefreshRate(): Long {
        return 1000L
    }

     fun getLabels(): List<ProcessLabel> {
        return listOf(
            ProcessLabel.PID,
            ProcessLabel.CPU_PERCENTAGE,
            ProcessLabel.NAME
        )
    }
}