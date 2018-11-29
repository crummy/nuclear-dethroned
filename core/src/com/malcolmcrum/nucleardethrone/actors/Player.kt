package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.*

class Player(val crosshair: Crosshair) : Actor() {
    val log = Log(Player::class.java)
    val texture = Texture("player.png")

    init {
        setBoundsCentered(0f, 0f, texture.width.toFloat(), texture.height.toFloat())
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.drawCentered(texture, x, y)
        crosshair.draw(batch, parentAlpha)
    }

    fun handleInput(input: Inputs) {
        when (input) {
            is MoveX -> this.x += input.x
            is MoveY -> this.y += input.y
            is Aim -> crosshair.setPosition(input.x, input.y)
        }
    }
}