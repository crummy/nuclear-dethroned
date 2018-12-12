package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.Collides
import com.malcolmcrum.nucleardethrone.Collision
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.events.*

class Player(val position: Vector2, val collisionCheck: (Collides) -> Collision) : Collides {

    val log = Log(Player::class)
    val sprite = Sprite(Texture("player.png"))
    val weapon = Weapon()
    val crosshair = Crosshair(position.x, position.y)

    init {
        sprite.setOrigin(0.5f, 0.5f)
        sprite.setScale(1/8f)
        val player = this
        EVENTS.register(object: EventListener<PlayerMovement> {
            override fun handle(event: PlayerMovement) {
                val velocity = Vector2(event.x.toFloat(), event.y.toFloat())
                val collision = collisionCheck.invoke(player)
                collision.modify(velocity)
                position.x += velocity.x * 0.2f
                position.y += velocity.y * 0.2f
                EVENTS.notify(PlayerPositionUpdated(position.x, position.y))
            }
        })
        EVENTS.register(object: EventListener<MouseDown> {
            override fun handle(event: MouseDown) {
                shoot(event.x, event.y)
            }
        })
    }

    fun draw(batch: Batch) {
        sprite.setOriginBasedPosition(position.x, position.y)
        sprite.draw(batch)
        drawDebug(batch)
        weapon.draw(batch)
        crosshair.draw(batch)
    }

    override fun getBoundary(): Rectangle {
        return Rectangle(position.x - 0.5f, position.y - 0.5f, 1f, 1f)
    }

    private fun shoot(x: Float, y: Float) {
        val position = Vector2(position.x, position.y)
        val velocity = Vector2(x, y).sub(position).nor().scl(2f)
        EVENTS.notify(BulletFired(position, velocity, playerFriendly = true))
    }
}

class Weapon {
    private var aimPosition: Vector2 = Vector2()
    private var player: Vector2 = Vector2()

    val texture = Texture("pistol.png")
    val sprite = Sprite(texture)

    init {
        sprite.setOrigin(0.5f, 0.5f)
        sprite.setScale(1/8f)
        EVENTS.register(object: EventListener<PlayerPositionUpdated> {
            override fun handle(event: PlayerPositionUpdated) {
                player.x = event.x
                player.y = event.y
            }
        })
        EVENTS.register(object: EventListener<MouseAimed> {
            override fun handle(event: MouseAimed) {
                aimPosition.x = event.x
                aimPosition.y = event.y
            }
        })
        sprite.scale(1/8f)
    }

    fun draw(batch: Batch) {
        val toCrosshair = Vector2(aimPosition.x, aimPosition.y).sub(player.x, player.y).nor().scl(0.5f)
        val playerCenter = Vector2(player.x + 0.5f, player.y + 0.5f)
        sprite.setOrigin(-0.25f, 0f)
        sprite.setOriginBasedPosition(playerCenter.x + toCrosshair.x, playerCenter.y + toCrosshair.y)
        sprite.rotation = toCrosshair.angle()
        sprite.draw(batch)
    }
}