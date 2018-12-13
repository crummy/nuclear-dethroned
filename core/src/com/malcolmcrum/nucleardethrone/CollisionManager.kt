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

data class Collision(val collidedHorizontally: Boolean, val collidedVertically: Boolean) {
    val collided = collidedHorizontally || collidedVertically

    fun modify(velocity: Vector2) {
        if (collidedHorizontally) velocity.x = 0f
        if (collidedVertically) velocity.y = 0f
    }
}

class CollisionManager(private val map: DesertMap) {
    fun checkCollision(entity: Collides, velocity: Vector2): Collision {
        val horizontalRect = entity.boundary.plus(Vector2(velocity.x, 0f))
        var collidedHorizontally = false
        (horizontalRect.bottom().toInt()..horizontalRect.top().toInt()).forEach { y ->
            if (velocity.movingRight) {
                val rightTile = map.rectangleAt(horizontalRect.right().toInt(), y)
                if (rightTile?.overlaps(horizontalRect) == true) {
                    collidedHorizontally = true
                    EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(horizontalRect, rightTile))
                }
            } else if (velocity.movingLeft) {
                val leftTile = map.rectangleAt(horizontalRect.left().toInt(), y)
                if (leftTile?.overlaps(horizontalRect) == true) {
                    collidedHorizontally = true
                    EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(horizontalRect, leftTile))
                }
            }
        }
        var collidedVertically = false
        val verticalRect = entity.boundary.plus(Vector2(0f, velocity.y))
        (verticalRect.left()..verticalRect.right() step 1f).forEach { x ->
            if (velocity.movingUp) {
                val topTile = map.rectangleAt(x.toInt(), verticalRect.top().toInt())
                if (topTile?.overlaps(verticalRect) == true) {
                    collidedVertically = true
                    EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(verticalRect, topTile))
                }
            } else if (velocity.movingDown) {
                val bottomTile = map.rectangleAt(x.toInt(), verticalRect.bottom().toInt())
                if (bottomTile?.overlaps(verticalRect) == true) {
                    collidedVertically = true
                    EVENTS.notify(com.malcolmcrum.nucleardethrone.events.Collision(verticalRect, bottomTile))
                }
            }
        }
        return Collision(collidedHorizontally, collidedVertically)
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