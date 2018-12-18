package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.ai.Action
import com.malcolmcrum.nucleardethrone.ai.Waiting
import java.time.Instant


const val MAX_WAIT = 100
const val CLOSE_DISTANCE = 0.1f

class Bandit(val position: Vector2) {
    val MAX_SPEED = 0.1f
    var action: Action
    val sprite = Sprite(Texture("bandit.png"))
    var destination: Vector2? = null
    var ticksSpentInAction = 0
    var lastShot = Instant.now()
    val velocity = Vector2()

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
        position.add(velocity)
    }
}