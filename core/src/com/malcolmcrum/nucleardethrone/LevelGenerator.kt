package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.random.Random

private val ROOM_COUNT = 6..12
private val ROOM_WIDTH = 4..16
private val ROOM_HEIGHT = 4..16

data class Level(val playerStart: Vector2, val walls: List<List<Boolean>>) {
    val width = walls[0].size
    val height = walls.size
    fun cell(x: Int, y: Int): Boolean {
        return walls[y][x]
    }
}

class LevelGenerator(val levelWidth: Int,
                     val levelHeight: Int) {

    fun generate(): Level {
        assert(levelWidth >= ROOM_WIDTH.endInclusive)
        assert(levelHeight >= ROOM_HEIGHT.endInclusive)
        val walls = createEmptyMap()
        val rooms = (0..ROOM_COUNT.random()).map { createRoom() }
        rooms.forEach { clearRoom(walls, it) }
        val playerStart = rooms.random().randomPoint()
        return Level(playerStart, walls)
    }

    private fun createEmptyMap(): MutableList<MutableList<Boolean>> {
        return Array(levelHeight) { Array(levelWidth) { true }.toMutableList() }.toMutableList()
    }

    private fun clearRoom(walls: MutableList<MutableList<Boolean>>, room: Rectangle) {
        (room.y.toInt() until room.y.toInt() + room.height.toInt() - 1).forEach { y ->
            (room.x.toInt() until room.x.toInt() + room.height.toInt() - 1).forEach { x ->
                walls[y][x] = false
            }
        }
    }

    private fun createRoom(): Rectangle {
        val roomWidth = ROOM_WIDTH.random()
        val roomHeight = ROOM_HEIGHT.random()
        val roomX = Random.nextInt(levelWidth - roomWidth)
        val roomY = Random.nextInt(levelHeight - roomHeight)
        return Rectangle(roomX.toFloat(), roomY.toFloat(), roomWidth.toFloat(), roomHeight.toFloat())
    }
}

