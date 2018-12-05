package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

fun Batch.drawCentered(texture: Texture, x: Float, y: Float) {
    this.draw(texture, x - texture.width / 2f, y - texture.height / 2f)
}

fun Actor.setBoundsCentered(x: Float, y: Float, width: Float, height: Float) {
    this.setBounds(x - width/2f, y  - height/2f, width, height)
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