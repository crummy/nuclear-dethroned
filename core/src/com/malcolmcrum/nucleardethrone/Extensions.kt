package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.scenes.scene2d.Actor

fun Actor.setBoundsCentered(x: Float, y: Float, width: Float, height: Float) {
    this.setBounds(x - width/2, y  - height/2, width, height)
}

// from https://stackoverflow.com/a/44332139/281657
infix fun ClosedFloatingPointRange<Float>.step(step: Float): Iterable<Float> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Float.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}