package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import kotlin.random.Random

sealed class Action
data class Moving(val x: Float, val y: Float) : Action()
data class Waiting(val ticksLeft: Int = Random.nextInt(MAX_WAIT)) : Action()

const val MAX_WAIT = 100
const val CLOSE_DISTANCE = 0.1f

class Bandit(val position: Vector2) {
    var action: Action
    val sprite = Sprite(Texture("bandit.png"))

    init {
        sprite.setOrigin(0.5f, 0.5f)
        sprite.setScale(1/8f)
        action = Waiting()
    }

    fun draw(batch: Batch) {
        sprite.setPosition(position.x, position.y)
        sprite.draw(batch)
    }

    fun update() {
        val nextAction = nextAction(action)
        if (nextAction is Moving) {
            val destination = Vector2(nextAction.x, nextAction.y)
            val toDestination = destination.sub(position).nor()
            position.x += toDestination.x
            position.y += toDestination.y
        }
        action = nextAction
    }

    private fun nextAction(action: Action): Action {
        return when (action) {
            is Moving -> if (position.dst(action.x, action.y) < CLOSE_DISTANCE) Waiting() else action
            is Waiting -> {
                val ticksLeft = action.ticksLeft - 1
                if (ticksLeft == 0) Moving(Random.nextFloat() * 200, Random.nextFloat() * 200) else Waiting(ticksLeft)
            }
        }
    }
}