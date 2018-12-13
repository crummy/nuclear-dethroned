package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.Collides
import com.malcolmcrum.nucleardethrone.Collision
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.events.BulletFired
import com.malcolmcrum.nucleardethrone.events.BulletImpact
import com.malcolmcrum.nucleardethrone.events.EventListener

class BulletManager(private val checkCollision: (Collides, Vector2) -> Collision) : EventListener<BulletFired> {
    val texture = Texture("bullet.png")

    private val bullets: MutableList<Bullet> = ArrayList()

    init {
        EVENTS.register(this)
    }

    override fun handle(event: BulletFired) {
        bullets.add(Bullet(event.position, event.velocity, texture))
    }

    fun draw(batch: Batch) {
        for (bullet in bullets) {
            bullet.update()
            bullet.draw(batch)
        }
        val collidedBullets = bullets.filter { checkCollision(it, it.velocity).collided }
        collidedBullets.forEach { EVENTS.notify(BulletImpact(it.position)) }
        bullets.removeAll(collidedBullets)
    }
}

class Bullet(val position: Vector2, val velocity: Vector2, texture: Texture) : Collides {
    private val sprite = Sprite(texture)

    init {
        sprite.setOrigin(0.5f, 0.5f)
        sprite.setScale(1/8f)
    }

    fun update() {
        position.add(velocity)
    }

    fun draw(batch: Batch) {
        sprite.rotation = velocity.angle()
        sprite.setPosition(position.x, position.y)
        sprite.draw(batch)
        drawDebug(batch)
    }

    override val boundary: Rectangle
        get() = sprite.boundingRectangle
}