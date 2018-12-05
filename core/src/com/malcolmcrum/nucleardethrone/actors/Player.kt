package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.*

class Player(val crosshair: Crosshair) : Actor() {
    val log = Log(Player::class.java)
    val texture = Texture("player.png")
    val weapon = Weapon(this, crosshair)

    init {
        setBoundsCentered(100f, 100f, texture.width.toFloat(), texture.height.toFloat())
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
        weapon.draw(batch)
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

class Weapon(private val player: Actor, private val crosshair: Crosshair) {
    val texture = Texture("pistol.png")
    val sprite = Sprite(texture)

    fun draw(batch: Batch) {
        val toCrosshair = Vector2(crosshair.x, crosshair.y).sub(player.x, player.y).nor().scl(4f)
        val playerCenter = Vector2(player.x + player.width / 2f, player.y + player.height / 2f)
        sprite.setOrigin(-8f, 0f)
        sprite.setOriginBasedPosition(playerCenter.x + toCrosshair.x, playerCenter.y + toCrosshair.y)
        sprite.rotation = toCrosshair.angle()
        sprite.draw(batch)
    }
}