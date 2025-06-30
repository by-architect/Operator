package com.byarchitect.operator.common.model.math

data class RootResult(
    val method: IterativeMethod,
    val root: Double,
    val iterations: Int,
    val convergenceRate: Double,
    val finalError: Double,
    val converged: Boolean
)