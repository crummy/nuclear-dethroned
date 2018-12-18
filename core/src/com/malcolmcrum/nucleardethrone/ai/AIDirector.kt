package com.malcolmcrum.nucleardethrone.ai

import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.DesertMap
import com.malcolmcrum.nucleardethrone.Log
import com.malcolmcrum.nucleardethrone.actors.Bandit

class AIDirector(private val bandits: Collection<Bandit>,
                 map: DesertMap,
                 playerPosition: () -> Vector2) {
    private val log: Log = Log(AIDirector::class)

    val actions = listOf(
            Moving(), Fleeing(playerPosition, map), Shooting(map, playerPosition), Waiting(), MoveToRandomDestination(map)
    )

    fun update() {
        for (bandit in bandits) {
            val done = bandit.action.perform(bandit)
            bandit.ticksSpentInAction++
            val lastAction = bandit.action.toString()
            if (done) {
                val suitableActions = actions.filter { action ->
                    action.preconditions.all { precondition ->
                        precondition.invoke(bandit)
                    }
                }.sortedBy { it.priority }
                bandit.action = suitableActions.shuffled().first()
                log.info("Bandit changed action from $lastAction to ${bandit.action} after ${bandit.ticksSpentInAction} ticks")
                bandit.ticksSpentInAction = 0
            }
        }
    }
}