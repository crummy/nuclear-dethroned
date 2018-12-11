package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import kotlin.random.Random

private val ROOM_COUNT = 6..12
private val ROOM_WIDTH = 4..16
private val ROOM_HEIGHT = 4..16

class MapGenerator(val layer: TiledMapTileLayer,
                   val cell: TiledMapTileLayer.Cell,
                   val width: Int,
                   val height: Int,
                   val worldWidth: Int,
                   val worldHeight: Int) {

    fun generate() {
        assert(width >= ROOM_WIDTH.endInclusive)
        assert(height >= ROOM_HEIGHT.endInclusive)
        fillMap()
        val rooms = ROOM_COUNT.random()
        repeat(rooms) {
            createRoom()
        }

    }

    private fun createRoom() {
        val roomWidth = ROOM_WIDTH.random()
        val roomHeight = ROOM_HEIGHT.random()
        val roomX = worldWidth / 2 - width / 2 + Random.nextInt(width - roomWidth)
        val roomY = worldHeight / 2 - height / 2 + Random.nextInt(height - roomHeight)
        (0..roomWidth).forEach { x ->
            (0..roomHeight).forEach { y ->
                layer.setCell(roomX + x, roomY + y, null)
            }
        }
    }

    private fun fillMap() {
        (0..worldWidth).forEach { x ->
            (0..worldHeight).forEach { y ->
                layer.setCell(x, y, cell)
            }
        }
    }
}

