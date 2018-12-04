package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.*

class Player(val crosshair: Crosshair) : Actor() {
    val log = Log(Player::class.java)
    val texture = Texture("player.png")

    init {
        setPosition(100f, 100f)
        setBoundsCentered(100f, 100f, texture.width.toFloat(), texture.height.toFloat())
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
        crosshair.draw(batch, parentAlpha)
    }

    fun handleInput(input: Inputs, collisionCheck: (Vector2) -> Vector2) {
        val velocity = Vector2()
        when (input) {
            is MoveX -> velocity.x = input.x
            is MoveY -> velocity.y = input.y
            is Aim -> crosshair.setPosition(input.x, input.y)
        }
        val collisionModifier = collisionCheck.invoke(velocity)
        this.x += velocity.x * collisionModifier.x
        this.y += velocity.y * collisionModifier.y
    }
}