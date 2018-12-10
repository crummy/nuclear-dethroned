package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.events.EventListener
import com.malcolmcrum.nucleardethrone.events.MouseAimed

class Crosshair : EventListener<MouseAimed>, Actor() {
    val log = Log(Crosshair::class)
    val texture = Texture("crosshair.png")

    init {
        setPosition(100f, 100f)
        setBounds(0f, 0f, texture.width.toFloat(), texture.height.toFloat())
        EVENTS.register(this)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x - texture.height / 2f, y - texture.width / 2f)
    }

    override fun handle(event: MouseAimed) {
        setPosition(event.x, event.y)
    }
}