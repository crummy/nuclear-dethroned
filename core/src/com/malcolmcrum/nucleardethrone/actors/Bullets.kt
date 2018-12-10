package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.events.BulletFired
import com.malcolmcrum.nucleardethrone.events.EventListener

class BulletManager : EventListener<BulletFired> {
    val texture = Texture("bullet.png")

    private val bullets: MutableList<Bullet> = ArrayList()

    init {
        EVENTS.register(this)
    }

    override fun handle(event: BulletFired) {
        bullets.add(Bullet(event.position, event.velocity))
    }

    fun draw(batch: Batch) {
        batch.begin()
        bullets.forEach { it.update() }
        bullets.forEach { batch.draw(texture, it.x, it.y) }
        batch.end()
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