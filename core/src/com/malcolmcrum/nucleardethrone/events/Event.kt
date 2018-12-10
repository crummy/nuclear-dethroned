package com.malcolmcrum.nucleardethrone.events

import com.badlogic.gdx.math.Vector2

sealed class Event
data class PlayerHit(val damage: Int) : Event()
data class MouseAimed(val x: Float, val y: Float): Event()
data class MouseClicked(val x: Float, val y: Float): Event()
data class PlayerMovement(val x: Int, val y: Int): Event()
data class PlayerPositionUpdated(val x: Float, val y: Float): Event()
data class BulletFired(val position: Vector2, val velocity: Vector2, val playerFriendly: Boolean) : Event()