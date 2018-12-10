package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.events.*
import com.malcolmcrum.nucleardethrone.setBoundsCentered

class Player(val collisionCheck: (Vector2) -> Vector2) : EventListener<PlayerMovement>, Actor() {

    val log = Log(Player::class)
    val texture = Texture("player.png")
    val weapon = Weapon(texture.width, texture.height)

    init {
        setBoundsCentered(100f, 100f, texture.width.toFloat(), texture.height.toFloat())
        EVENTS.register(this)
        EVENTS.register(object: EventListener<MouseClicked> {
            override fun handle(event: MouseClicked) {
                shoot(event.x, event.y)
            }
        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
        weapon.draw(batch)
    }

    override fun handle(event: PlayerMovement) {
        val velocity = Vector2(event.x.toFloat(), event.y.toFloat())
        val collisionModifier = collisionCheck.invoke(velocity)
        x += velocity.x * collisionModifier.x
        y += velocity.y * collisionModifier.y
        EVENTS.notify(PlayerPositionUpdated(x, y))
    }

    private fun shoot(x: Float, y: Float) {
        val position = Vector2(this.x, this.y)
        val velocity = Vector2(x, y).sub(position).nor().scl(2f)
        EVENTS.notify(BulletFired(position, velocity, playerFriendly = true))
    }
}

class Weapon(private val playerWidth: Int, private val playerHeight: Int) {
    private var crosshair: Vector2 = Vector2()
    private var player: Vector2 = Vector2()

    val texture = Texture("pistol.png")
    val sprite = Sprite(texture)

    init {
        EVENTS.register(object: EventListener<PlayerPositionUpdated> {
            override fun handle(event: PlayerPositionUpdated) {
                player.x = event.x
                player.y = event.y
            }
        })
        EVENTS.register(object: EventListener<MouseAimed> {
            override fun handle(event: MouseAimed) {
                crosshair.x = event.x
                crosshair.y = event.y
            }
        })
    }

    fun draw(batch: Batch) {
        val toCrosshair = Vector2(crosshair.x, crosshair.y).sub(player.x, player.y).nor().scl(4f)
        val playerCenter = Vector2(player.x + playerWidth / 2f, player.y + playerHeight / 2f)
        sprite.setOrigin(-2f, 0f)
        sprite.setOriginBasedPosition(playerCenter.x + toCrosshair.x, playerCenter.y + toCrosshair.y)
        sprite.rotation = toCrosshair.angle()
        sprite.draw(batch)
    }
}