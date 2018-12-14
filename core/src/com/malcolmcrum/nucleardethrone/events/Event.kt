package com.malcolmcrum.nucleardethrone.events

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.Collider

sealed class Event
data class PlayerHit(val damage: Int) : Event()
data class MouseAimed(val x: Float, val y: Float): Event()
data class MouseDragged(val x: Float, val y: Float): Event()
data class MouseDown(val x: Float, val y: Float): Event()
data class MouseUp(val x: Float, val y: Float): Event()
data class PlayerMovement(val x: Int, val y: Int): Event()
data class PlayerPositionUpdated(val x: Float, val y: Float): Event()
data class BulletFired(val position: Vector2, val velocity: Vector2, val playerFriendly: Boolean) : Event()
data class CollidedWithWall(val first: Rectangle, val second: Rectangle) : Event()
class BulletImpactWall(val position: Vector2, val x: Int, val y: Int) : Event()
class BulletImpactCollider(val position: Vector2, val collider: Collider) : Event()
