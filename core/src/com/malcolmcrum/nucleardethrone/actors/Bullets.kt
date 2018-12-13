package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
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
    private val destroyedBullets = mutableListOf<Vector2>()

    init {
        EVENTS.register(this)
        EVENTS.register(object: EventListener<BulletImpact> {
            override fun handle(event: BulletImpact) {
                destroyedBullets.add(event.position)
            }
        })
    }

    override fun handle(event: BulletFired) {
        bullets.add(Bullet(event.position, event.velocity, texture))
    }

    fun draw(batch: Batch) {
        val collidedBullets = mutableListOf<Bullet>()
        for (bullet in bullets) {
            bullet.update()
            bullet.draw(batch)
            val collision = checkCollision(bullet, bullet.velocity)
            if (collision.collided) {
                val wall = collision.nearestCollision(bullet.position)
                EVENTS.notify(BulletImpact(Vector2(wall.first.toFloat(), wall.second.toFloat())))
                collidedBullets.add(bullet)
            }
        }
        bullets.removeAll(collidedBullets)
        drawDestroyedBullets(batch)
    }

    private fun drawDestroyedBullets(batch: Batch) {
        batch.end()
        val shapeRenderer = ShapeRenderer()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.color = Color.BLUE
        for (bullet in destroyedBullets) {
            shapeRenderer.circle(bullet.x, bullet.y, 0.1f, 8)
        }
        shapeRenderer.end()
        batch.begin()
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