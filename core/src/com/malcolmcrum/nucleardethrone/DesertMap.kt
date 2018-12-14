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

const val BLOCKING: String = "BLOCKING"
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
    val blockingTexture = TextureRegion(texture, 8, 8, 8, 8)
    val bottomTrimTexture = TextureRegion(texture, 8, 16, 8, 8)
    val topTrimTexture = TextureRegion(texture, 8, 0, 8, 8)
    val leftTrimTexture = TextureRegion(texture, 0, 8, 8, 8)
    val rightTrimTexture = TextureRegion(texture, 16, 8, 8, 8)
    val blockingTile = StaticTiledMapTile(blockingTexture)
    val bottomTrimTile = StaticTiledMapTile(bottomTrimTexture)
    val topTrimTile = StaticTiledMapTile(topTrimTexture)
    val leftTrimTile = StaticTiledMapTile(leftTrimTexture)
    val rightTrimTile = StaticTiledMapTile(rightTrimTexture)
    val blockingLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)
    val trimmingLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, 8, 8)

    init {
        blockingTile.properties.put(BLOCKING, true)
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
        map.layers.add(trimmingLayer)

        EVENTS.register(object: EventListener<BulletImpactWall> {
            override fun handle(event: BulletImpactWall) {
                blockingLayer.setCell(event.x, event.y, null)
                addTrimmings()
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
                blockingLayer.getCell(x, y)?.let {
                    if (blockingLayer.getCell(x - 1, y) == null) trimmingLayer.setCell(x - 1, y, leftTrimCell)
                    if (blockingLayer.getCell(x + 1, y) == null) trimmingLayer.setCell(x + 1, y, rightTrimCell)
                    if (blockingLayer.getCell(x, y - 1) == null) trimmingLayer.setCell(x, y - 1, bottomTrimCell)
                    if (blockingLayer.getCell(x, y + 1) == null) trimmingLayer.setCell(x, y + 1, topTrimCell)
                } ?: run {
                    trimmingLayer.setCell(x, y, null)
                }
            }
        }
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

fun TiledMapTile?.isBlocking(): Boolean {
    return this != null && properties.get(BLOCKING) as? Boolean == true
}