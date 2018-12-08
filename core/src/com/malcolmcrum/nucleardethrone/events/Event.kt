package com.malcolmcrum.nucleardethrone.events

sealed class Event
data class PlayerHit(val damage: Int) : Event()
data class MouseAimed(val x: Float, val y: Float): Event()
data class MouseClicked(val x: Float, val y: Float): Event()
data class PlayerMovement(val x: Int, val y: Int): Event()
data class PlayerPositionUpdated(val x: Float, val y: Float): Event()