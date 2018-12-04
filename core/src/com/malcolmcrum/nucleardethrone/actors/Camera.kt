package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.viewport.FitViewport
import com.malcolmcrum.nucleardethrone.Log

val log = Log(Camera::class.java)

class Camera(private val player: Player, private val crosshair: Crosshair) : Actor() {
    val viewport = FitViewport(160f, 100f)

    fun update() {
        val position = Vector2(player.x, player.y).lerp(Vector2(crosshair.x, crosshair.y), 0.25f)
        viewport.camera.position.set(position.x, position.y, 0f)
        viewport.camera.update()
    }
}