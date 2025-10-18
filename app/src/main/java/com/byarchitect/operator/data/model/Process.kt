package com.byarchitect.operator.data.model

data class Process(
    val pid: Int,
    val packageName: String,
    val cpuPercentage: Float,
    val memPercentage: Float

) {

}