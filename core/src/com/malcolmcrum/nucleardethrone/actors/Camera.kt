package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.viewport.FitViewport
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.events.EventListener
import com.malcolmcrum.nucleardethrone.events.MouseAimed
import com.malcolmcrum.nucleardethrone.events.PlayerPositionUpdated

class Camera : Actor() {

    private val log = Log(Camera::class)

    private val player = Vector2()
    private val crosshair = Vector2()
    val viewport = FitViewport(160f, 100f)

    init {
        EVENTS.register(object: EventListener<MouseAimed> {
            override fun handle(event: MouseAimed) {
                crosshair.x = event.x
                crosshair.y = event.y
            }
        })
        EVENTS.register(object: EventListener<PlayerPositionUpdated> {
            override fun handle(event: PlayerPositionUpdated) {
                player.x = event.x
                player.y = event.y
            }
        })
    }

    fun update() {
        val position = Vector2(player.x, player.y).lerp(Vector2(crosshair.x, crosshair.y), 0.25f)
        viewport.camera.position.lerp(Vector3(position.x, position.y, 0f), 0.25f)
        viewport.camera.update()
    }
}