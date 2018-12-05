package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.random.Random

sealed class Action
data class Moving(val x: Float, val y: Float) : Action()
data class Waiting(val ticksLeft: Int = Random.nextInt(MAX_WAIT)) : Action()

const val MAX_WAIT = 100
const val CLOSE_DISTANCE = 0.1f

class Bandit(private val collisionCheck: (x: Float, y: Float) -> Boolean) : Actor() {
    var action: Action
    val texture = Texture("bandit.png")

    init {
        val location = pickGoodLocation()
        setPosition(location.x, location.y)
        action = Waiting()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }

    fun update() {
        val nextAction = nextAction(action)
        if (nextAction is Moving) {
            val position = Vector2(x, y)
            val destination = Vector2(nextAction.x, nextAction.y)
            val toDestination = destination.sub(position).nor()
            x += toDestination.x
            y += toDestination.y
        }
        action = nextAction
    }

    private fun nextAction(action: Action): Action {
        return when (action) {
            is Moving -> if (Vector2(x, y).dst(action.x, action.y) < CLOSE_DISTANCE) Waiting() else action
            is Waiting -> {
                val ticksLeft = action.ticksLeft - 1
                if (ticksLeft == 0) Moving(Random.nextFloat() * 200, Random.nextFloat() * 200) else Waiting(ticksLeft)
            }
        }
    }

    private fun pickGoodLocation(): Vector2 {
        for (attempt in 0..100) {
            x = Random.nextFloat() * 200
            y = Random.nextFloat() * 200
            if (!collisionCheck(x, y))
                return Vector2(x, y)
        }
        throw Exception("Could not find good location")
    }
}