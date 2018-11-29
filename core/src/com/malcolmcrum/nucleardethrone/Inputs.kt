package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

sealed class Inputs
data class MoveX(val x: Float) : Inputs()
data class MoveY(val y: Float) : Inputs()
data class Aim(val x: Float, val y: Float) : Inputs()
data class Shoot(val ignored: Float) : Inputs()

fun getInputs(camera: Viewport): Collection<Inputs> {
    val inputs = HashSet<Inputs>()
    if (Gdx.input.isKeyPressed(W)) {
        inputs.add(MoveY(1f))
    }
    if (Gdx.input.isKeyPressed(S)) {
        inputs.add(MoveY(-1f))
    }
    if (Gdx.input.isKeyPressed(A)) {
        inputs.add(MoveX(-1f))
    }
    if (Gdx.input.isKeyPressed(D)) {
        inputs.add(MoveX(1f))
    }
    val aimPosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
    camera.unproject(aimPosition)
    inputs.add(Aim(aimPosition.x, aimPosition.y))
    if (Gdx.input.isKeyPressed(LEFT)) {
        inputs.add(Shoot(0f))
    }
    return inputs
}