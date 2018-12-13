package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.events.EventListener

interface Collides {
    val boundary: Rectangle
    fun drawDebug(batch: Batch) {
        batch.end()
        val shapeRenderer = ShapeRenderer()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.color = Color.WHITE
        shapeRenderer.rect(boundary.x, boundary.y, boundary.width, boundary.height)
        shapeRenderer.end()
        batch.begin()
    }
}

data class Collision(val up: Boolean, val down: Boolean, val left: Boolean, val right: Boolean) {
    fun modify(velocity: Vector2) {
        if (velocity.x > 0 && right) velocity.x = 0f
        if (velocity.x < 0 && left) velocity.x = 0f
        if (velocity.y > 0 && up) velocity.y = 0f
        if (velocity.y < 0 && down) velocity.y = 0f
    }
}

class CollisionManager(private val map: DesertMap) {
    fun checkCollision(entity: Collides, velocity: Vector2): Collision {
        val rect = entity.boundary.plus(velocity)
        var up = false
        var down = false
        var left = false
        var right = false
        (rect.bottom().toInt()..rect.top().toInt()).forEach { y ->
            val rightTile = map.rectangleAt(rect.right().toInt(), y)
            if (rightTile?.overlaps(rect) == true) {
                right = true
                EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(rect, rightTile))
            }
            val leftTile = map.rectangleAt(rect.left().toInt(), y)
            if (leftTile?.overlaps(rect) == true) {
                left = true
                EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(rect, leftTile))
            }
        }
        (rect.left()..rect.right() step 1f).forEach { x ->
            val topTile = map.rectangleAt(x.toInt(), rect.top().toInt())
            if (topTile?.overlaps(rect) == true) {
                up = true
                EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(rect, topTile))
            }
            val bottomTile = map.rectangleAt(x.toInt(), rect.bottom().toInt())
            if (bottomTile?.overlaps(rect) == true) {
                down = true
                EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(rect, bottomTile))
            }
        }
        return Collision(up, down, left, right)
    }
}

class CollisionDebugger {
    val collisions = mutableListOf<com.malcolmcrum.nucleardethrone.events.Collision>()

    init {
        EVENTS.register(object: EventListener<com.malcolmcrum.nucleardethrone.events.Collision> {
            override fun handle(event: com.malcolmcrum.nucleardethrone.events.Collision) {
                collisions.add(event)
            }
        })
    }

    fun draw(batch: Batch) {
        for (collision in collisions) {
            batch.end()
            val shapeRenderer = ShapeRenderer()
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
            shapeRenderer.projectionMatrix = batch.projectionMatrix
            shapeRenderer.color = Color.YELLOW
            shapeRenderer.rect(collision.first.x, collision.first.y, collision.first.width, collision.first.height)
            shapeRenderer.color = Color.RED
            shapeRenderer.rect(collision.second.x, collision.second.y, collision.second.width, collision.second.height)
            shapeRenderer.end()
            batch.begin()
        }
        collisions.clear()
    }
}

val Vector2.movingRight
    get() = x > 0

val Vector2.movingLeft
    get() = x < 0

val Vector2.movingUp
    get() = y > 0

val Vector2.movingDown
    get() = y < 0

fun Rectangle.plus(v: Vector2): Rectangle {
    return Rectangle(x + v.x, y + v.y, width, height)
}