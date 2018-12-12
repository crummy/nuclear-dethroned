package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.events.EventListener
import com.malcolmcrum.nucleardethrone.events.MouseAimed

class Crosshair(x: Float, y: Float) : EventListener<MouseAimed> {
    val log = Log(Crosshair::class)
    val sprite = Sprite(Texture("crosshair.png"))

    init {
        sprite.setOrigin(0.5f, 0.5f)
        sprite.setScale(1/8f)
        sprite.setPosition(x, y)
        EVENTS.register(this)
    }

    fun draw(batch: Batch) {
        sprite.draw(batch)
    }

    override fun handle(event: MouseAimed) {
        sprite.setPosition(event.x, event.y)
    }
}