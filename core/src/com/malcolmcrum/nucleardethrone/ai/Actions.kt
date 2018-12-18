package com.malcolmcrum.nucleardethrone.ai

import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.DesertMap
import com.malcolmcrum.nucleardethrone.EVENTS
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.actors.Bandit
import com.malcolmcrum.nucleardethrone.events.BulletFired
import java.time.Duration
import java.time.Instant
import kotlin.random.Random


abstract class Action {
    val log = Log(Action::class)
    abstract val priority: Int
    abstract val preconditions: Collection<(Bandit) -> Boolean>
    abstract fun perform(bandit: Bandit): Boolean // return true if done
}

open class Moving : Action() {
    val CLOSE_ENOUGH = 0.2f

    override val priority: Int = 1

    override val preconditions: Collection<(Bandit) -> Boolean> = listOf(
            { bandit -> bandit.destination != null }
    )

    override fun perform(bandit: Bandit): Boolean {
        bandit.apply {
            val toDestination = destination!!.cpy().sub(position)
            velocity.set(toDestination.nor().scl(bandit.MAX_SPEED))
            if (position.dst(destination) <= CLOSE_ENOUGH) {
                log.info("Bandit $bandit reached destination $destination")
                destination = null
                velocity.set(0f, 0f)
            }
            return destination == null
        }
    }
}

class Fleeing(val player: () -> Vector2, val map: DesertMap) : Action() {
    val TOO_CLOSE_DISTANCE = 1f

    override val priority = 2

    override val preconditions: Collection<(Bandit) -> Boolean> = listOf(
            { bandit -> player.invoke().dst(bandit.position) < TOO_CLOSE_DISTANCE }
    )

    override fun perform(bandit: Bandit): Boolean {
        bandit.apply {
            destination = position.cpy().sub(player.invoke()).nor().scl(4f) // TODO: check we can safely move here
            log.info("Bandit $bandit at $position avoiding player, moving towards $destination")
        }
        return true
    }

}

class MoveToRandomDestination(val map: DesertMap) : Action() {
    override val priority = 3

    override val preconditions: Collection<(Bandit) -> Boolean> = listOf()

    override fun perform(bandit: Bandit): Boolean {
        bandit.apply {
            destination = position.cpy().add(Vector2().setToRandomDirection().scl(2f)) //  // TODO: check we can safely move here
            log.info("Bandit $bandit at $position moving to random location: $destination")
        }
        return true
    }

}

class Waiting : Action() {
    val TICK_LIMIT = (100..1000)
    override val priority = 3

    override val preconditions: Collection<(Bandit) -> Boolean> = listOf()

    override fun perform(bandit: Bandit): Boolean {
        bandit.apply {
            if (ticksSpentInAction > TICK_LIMIT.first) {
                return Random.nextInt(TICK_LIMIT.last - TICK_LIMIT.first) < ticksSpentInAction
            } else {
                return false
            }
        }
    }
}

class Shooting(val map: DesertMap, val player: () -> Vector2) : Action() {
    val MIN_TIME_BETWEEN_SHOTS = Duration.ofSeconds(1)

    override val priority = 3

    override val preconditions: Collection<(Bandit) -> Boolean> = listOf(
            { bandit -> (bandit.lastShot + MIN_TIME_BETWEEN_SHOTS).isBefore(Instant.now()) }
            // TODO: ensure bandit has line of sight to player
    )

    override fun perform(bandit: Bandit): Boolean {
        val velocity = player.invoke().cpy().sub(bandit.position).nor().scl(0.25f)
        EVENTS.notify(BulletFired(bandit.position.cpy(), velocity, playerFriendly = false))
        bandit.lastShot = Instant.now()
        return true
    }
}