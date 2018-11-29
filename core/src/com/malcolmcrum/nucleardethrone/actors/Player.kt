package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.malcolmcrum.nucleardethrone.Log

class Player(val crosshair: Crosshair) : Actor() {
    val log = Log(Player::class.java)
    val texture = Texture("player.png")

    init {
        setBounds(x, y, texture.width.  toFloat(), texture.height.toFloat())
        addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                TODO()
            }

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                log.info("mouse $x, $y")
                setX(x)
                setY(y)
                return true
            }
        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
        x += 1
    }
}