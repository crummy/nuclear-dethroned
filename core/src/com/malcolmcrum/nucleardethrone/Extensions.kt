package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.random.Random

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

fun Rectangle.randomPoint(): Vector2 {
    return Vector2(x + Random.nextInt(width.toInt()), y + Random.nextInt(height.toInt()))
}

fun Rectangle.bottom() = y
fun Rectangle.top() = y + height
fun Rectangle.left() = x
fun Rectangle.right() = x + width