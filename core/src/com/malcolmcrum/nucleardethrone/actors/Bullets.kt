package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.Collides
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.events.BulletFired
import com.malcolmcrum.nucleardethrone.events.EventListener

class BulletManager : EventListener<BulletFired>, Collides {
    val sprite = Sprite(Texture("bullet.png"))

    private val bullets: MutableList<Bullet> = ArrayList()

    init {
        sprite.setOrigin(0.5f, 0.5f)
        sprite.setScale(1/8f)
        EVENTS.register(this)
    }

    override fun handle(event: BulletFired) {
        bullets.add(Bullet(event.position, event.velocity))
    }

    fun draw(batch: Batch) {
        bullets.forEach { it.update() }
        bullets.forEach {
            sprite.rotation = it.velocity.angle()
            sprite.setPosition(it.x, it.y)
            sprite.draw(batch)
            drawDebug(batch)
        }
    }

    override fun getBoundary(): Rectangle {
        return sprite.boundingRectangle
    }

}

class Bullet(position: Vector2, val velocity: Vector2) : Actor() {
    init {
        x = position.x
        y = position.y
    }

    fun update() {
        x += velocity.x
        y += velocity.y
    }
}