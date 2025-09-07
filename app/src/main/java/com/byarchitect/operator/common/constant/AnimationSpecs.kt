package com.byarchitect.operator.common.constant

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

class AnimationSpecs {

    companion object {

        val containerAnimation: AnimationSpec<Float> =
            tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        val containerSlowAnimation : AnimationSpec<Float> =
            tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
    }
}