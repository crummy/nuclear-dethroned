package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.events.CollidedWithWall
import com.malcolmcrum.nucleardethrone.events.EventListener

interface Collider {
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

data class Collision(val collidedHorizontally: Boolean,
                     val collidedVertically: Boolean,
                     val collidedWalls: List<Pair<Int, Int>>) {

    val collided = collidedHorizontally || collidedVertically

    fun modify(velocity: Vector2) {
        if (collidedHorizontally) velocity.x = 0f
        if (collidedVertically) velocity.y = 0f
    }

    fun nearestCollision(v: Vector2) = collidedWalls.sortedBy { v.dst2(it.first.toFloat(), it.second.toFloat()) }.first()
}

class CollisionManager(private val map: DesertMap) {
    fun checkWallCollision(entity: Collider, velocity: Vector2): Collision {
        val horizontalRect = entity.boundary.plus(Vector2(velocity.x, 0f))
        var collidedHorizontally = false
        val collidedWalls = mutableListOf<Pair<Int, Int>>()
        (horizontalRect.bottom().toInt()..horizontalRect.top().toInt()).forEach { y ->
            if (velocity.movingRight) {
                val x = horizontalRect.right().toInt()
                val rightTile = map.rectangleAt(x, y)
                if (rightTile?.overlaps(horizontalRect) == true) {
                    collidedHorizontally = true
                    collidedWalls.add(Pair(x, y))
                    EVENTS.notify(CollidedWithWall(horizontalRect, rightTile))
                }
            } else if (velocity.movingLeft) {
                val x = horizontalRect.left().toInt()
                val leftTile = map.rectangleAt(x, y)
                if (leftTile?.overlaps(horizontalRect) == true) {
                    collidedHorizontally = true
                    collidedWalls.add(Pair(x, y))
                    EVENTS.notify(CollidedWithWall(horizontalRect, leftTile))
                }
            }
        }
        var collidedVertically = false
        val verticalRect = entity.boundary.plus(Vector2(0f, velocity.y))
        (verticalRect.left().toInt()..verticalRect.right().toInt()).forEach { x ->
            if (velocity.movingUp) {
                val y = verticalRect.top().toInt()
                val topTile = map.rectangleAt(x, y)
                if (topTile?.overlaps(verticalRect) == true) {
                    collidedVertically = true
                    collidedWalls.add(Pair(x, y))
                    EVENTS.notify(CollidedWithWall(verticalRect, topTile))
                }
            } else if (velocity.movingDown) {
                val y = verticalRect.bottom().toInt()
                val bottomTile = map.rectangleAt(x, y)
                if (bottomTile?.overlaps(verticalRect) == true) {
                    collidedVertically = true
                    collidedWalls.add(Pair(x, y))
                    EVENTS.notify(CollidedWithWall(verticalRect, bottomTile))
                }
            }
        }
        return Collision(collidedHorizontally, collidedVertically, collidedWalls)
    }
}

class CollisionDebugger {
    val collisions = mutableListOf<CollidedWithWall>()

    init {
        EVENTS.register(object: EventListener<CollidedWithWall> {
            override fun handle(event: CollidedWithWall) {
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