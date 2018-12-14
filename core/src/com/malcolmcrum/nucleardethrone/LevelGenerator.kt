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

// TODO: This should take entire world width/height into account, not DesertMap IMO
class LevelGenerator(val levelWidth: Int,
                     val levelHeight: Int) {

    fun generate(): Level {
        assert(levelWidth >= ROOM_WIDTH.endInclusive)
        assert(levelHeight >= ROOM_HEIGHT.endInclusive)
        val walls = createEmptyMap()
        val rooms = (0..ROOM_COUNT.random()).map { createRoom() }
        rooms.forEach { clearRoom(walls, it) }
        rooms.zipWithNext().forEach { clearHallway(walls, it.first, it.second) }
        val playerStart = rooms.random().randomPoint()
        return Level(playerStart, walls)
    }

    private fun clearHallway(walls: MutableList<MutableList<Boolean>>, first: Rectangle, second: Rectangle) {
        val origin = first.randomPoint()
        val destination = second.randomPoint()
        val horizontalFirst = Random.nextBoolean()
        if (horizontalFirst) {
            if (origin.x < destination.x) {
                (origin.x..destination.x step 1f).forEach { x ->
                    walls[origin.y.toInt()][x.toInt()] = true
                }
            } else {
                (destination.x..origin.x step 1f).forEach { x ->
                    walls[origin.y.toInt()][x.toInt()] = true
                }
            }
            if (origin.y < destination.y) {
                (origin.y..destination.y step 1f).forEach { y ->
                    walls[y.toInt()][destination.y.toInt()] = true
                }
            } else {
                (destination.y..origin.y step 1f).forEach { y ->
                    walls[y.toInt()][destination.x.toInt()] = true
                }
            }
        }
    }

    private fun createEmptyMap(): MutableList<MutableList<Boolean>> {
        return Array(levelHeight) { Array(levelWidth) { true }.toMutableList() }.toMutableList()
    }

    private fun clearRoom(walls: MutableList<MutableList<Boolean>>, room: Rectangle) {
        (room.bottom().toInt() until room.top().toInt()).forEach { y ->
            (room.left().toInt() until room.right().toInt()).forEach { x ->
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

