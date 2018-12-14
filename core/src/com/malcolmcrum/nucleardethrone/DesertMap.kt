package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.malcolmcrum.nucleardethrone.events.BulletImpactWall
import com.malcolmcrum.nucleardethrone.events.EventListener

const val TOTAL_MAP_WIDTH = 1024
const val TOTAL_MAP_HEIGHT = 1024

class DesertMap(private val level: Level) {
    val map: TiledMap = TiledMap()
    val texture = Texture("tile.png")
    val blockingCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val bottomTrimCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val topTrimCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val leftTrimCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val rightTrimCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val blockingTile = StaticTiledMapTile(TextureRegion(texture, 8, 8, 8, 8))
    val bottomTrimTile = StaticTiledMapTile(TextureRegion(texture, 8, 16, 8, 8))
    val topTrimTile = StaticTiledMapTile(TextureRegion(texture, 8, 0, 8, 8))
    val leftTrimTile = StaticTiledMapTile(TextureRegion(texture, 0, 8, 8, 8))
    val rightTrimTile = StaticTiledMapTile(TextureRegion(texture, 16, 8, 8, 8))
    val blockingLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)
    val leftTrimLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)
    val rightTrimLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)
    val bottomTrimLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)
    val topTrimLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)

    init {
        blockingTile.offsetY = 4f
        bottomTrimTile.offsetY = 4f
        topTrimTile.offsetY = 4f
        rightTrimTile.offsetY = 4f
        leftTrimTile.offsetY = 4f
        blockingCell.tile = blockingTile
        bottomTrimCell.tile = bottomTrimTile
        topTrimCell.tile = topTrimTile
        leftTrimCell.tile = leftTrimTile
        rightTrimCell.tile = rightTrimTile

        fillEntireMap()
        clearRooms()
        addTrimmings()
        map.layers.add(blockingLayer)
        map.layers.add(rightTrimLayer)
        map.layers.add(topTrimLayer)
        map.layers.add(leftTrimLayer)
        map.layers.add(bottomTrimLayer)

        EVENTS.register(object: EventListener<BulletImpactWall> {
            override fun handle(event: BulletImpactWall) {
                blockingLayer.setCell(event.x, event.y, null)
                for (x in (event.x - 1..event.x + 1)) {
                    for (y in (event.y - 1..event.y + 1)) {
                        addTrimmings(x, y)
                    }
                }

            }
        })
    }

    private fun fillEntireMap() {
        (0..TOTAL_MAP_WIDTH).forEach { x ->
            (0..TOTAL_MAP_HEIGHT).forEach { y ->
                blockingLayer.setCell(x, y, blockingCell)
            }
        }
    }

    private fun clearRooms() {
        (0 until level.width).forEach { x ->
            (0 until level.height).forEach { y ->
                if (!level.cell(x, y)) {
                    val cellX = TOTAL_MAP_WIDTH/2 - level.width/2 + x
                    val cellY = TOTAL_MAP_HEIGHT/2 - level.height/2 + y
                    blockingLayer.setCell(cellX, cellY, null)
                }
            }
        }
    }

    private fun addTrimmings() {
        (0..blockingLayer.width).forEach { x ->
            (0..blockingLayer.height).forEach { y ->
                if (blockingLayer.getCell(x, y) == null) {
                    addTrimmings(x, y)
                }
            }
        }
    }

    private fun addTrimmings(x: Int, y: Int) {
        val rightCell = if (blockingLayer.getCell(x - 1, y) != null) rightTrimCell else null
        val leftCell = if (blockingLayer.getCell(x + 1, y) != null) leftTrimCell else null
        val bottomCell = if (blockingLayer.getCell(x, y + 1) != null) bottomTrimCell else null
        val topCell = if (blockingLayer.getCell(x, y - 1) != null) topTrimCell else null
        leftTrimLayer.setCell(x, y, leftCell)
        rightTrimLayer.setCell(x, y, rightCell)
        bottomTrimLayer.setCell(x, y, bottomCell)
        topTrimLayer.setCell(x, y, topCell)
    }

    // TODO: Use this, not rectangleAt. It's cheaper and does the same
    fun tileAt(x: Int, y: Int): TiledMapTile? {
        return blockingLayer.getCell(x, y)?.tile
    }

    fun rectangleAt(x: Int, y: Int): Rectangle? {
        return blockingLayer.getCell(x, y)?.let { Rectangle(x.toFloat(), y.toFloat(), 1f, 1f) }
    }

    fun availablePosition(): Vector2 {
        (0 until TOTAL_MAP_HEIGHT).shuffled().forEach { y ->
            (0 until TOTAL_MAP_WIDTH).shuffled().forEach { x ->
                if (blockingLayer.getCell(x, y) == null) {
                    return Vector2(x.toFloat(), y.toFloat())
                }
            }
        }
        throw Exception("Couldn't find an available position")
    }

    val playerStart: Vector2 = level.playerStart.add(TOTAL_MAP_WIDTH/2f - level.width/2f, TOTAL_MAP_HEIGHT/2f - level.height/2f)
}