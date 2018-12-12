package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

interface Collides {
    fun getBoundary(): Rectangle
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
    fun checkCollision(entity: Collides): Collision {
        val rect = entity.getBoundary()
        var up = false
        var down = false
        var left = false
        var right = false
        (rect.bottom()..rect.top() step 1f).map { it.toInt() }.forEach{ y ->
            if (collidesWithWall(rect.right().toInt(), y)) right = true
            if (collidesWithWall(rect.left().toInt(), y)) left = true
        }
        (rect.left()..rect.right() step 1f).map { it.toInt() }.forEach { x ->
            if (collidesWithWall(x, rect.top().toInt())) up = true
            if (collidesWithWall(x, rect.bottom().toInt())) down = true
        }
        return Collision(up, down, left, right)
    }

    private fun collidesWithWall(x: Int, y: Int): Boolean {
        return map.tileAt(x, y).isBlocking()
    }
}