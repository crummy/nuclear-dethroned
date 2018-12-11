package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Vector2

const val BLOCKING: String = "BLOCKING"
const val TILE_SIZE = 8
const val TOTAL_MAP_WIDTH = 1024
const val TOTAL_MAP_HEIGHT = 1024
const val SPAWN_MAP_WIDTH = 128
const val SPAWN_MAP_HEIGHT = 128

class DesertMap {
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
    val blockingLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, TILE_SIZE, TILE_SIZE)
    val trimmingLayer = TiledMapTileLayer(TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT, TILE_SIZE, TILE_SIZE)

    init {
        blockingTile.properties.put(BLOCKING, true)
        blockingTile.offsetY = TILE_SIZE / 2f
        bottomTrimTile.offsetY = TILE_SIZE / 2f
        topTrimTile.offsetY = TILE_SIZE / 2f
        rightTrimTile.offsetY = TILE_SIZE / 2f
        leftTrimTile.offsetY = TILE_SIZE / 2f
        blockingCell.tile = blockingTile
        bottomTrimCell.tile = bottomTrimTile
        topTrimCell.tile = topTrimTile
        leftTrimCell.tile = leftTrimTile
        rightTrimCell.tile = rightTrimTile

        MapGenerator(blockingLayer, blockingCell, SPAWN_MAP_WIDTH, SPAWN_MAP_HEIGHT, TOTAL_MAP_WIDTH, TOTAL_MAP_HEIGHT).generate()
        addTrimmings()
        map.layers.add(blockingLayer)
        map.layers.add(trimmingLayer)
    }

    private fun addTrimmings() {
        (0..blockingLayer.width).forEach { x ->
            (0..blockingLayer.height).forEach { y ->
                blockingLayer.getCell(x, y)?.let {
                    if (blockingLayer.getCell(x - 1, y) == null) trimmingLayer.setCell(x - 1, y, leftTrimCell)
                    if (blockingLayer.getCell(x + 1, y) == null) trimmingLayer.setCell(x + 1, y, rightTrimCell)
                    if (blockingLayer.getCell(x, y - 1) == null) trimmingLayer.setCell(x, y - 1, bottomTrimCell)
                    if (blockingLayer.getCell(x, y + 1) == null) trimmingLayer.setCell(x, y + 1, topTrimCell)
                }
            }
        }
    }

    fun tileAt(x: Float, y: Float): TiledMapTile? {
        return blockingLayer.getCell((x / TILE_SIZE).toInt(), (y / TILE_SIZE).toInt())?.tile
    }

    fun availablePosition(): Vector2 {
        (0 until TOTAL_MAP_HEIGHT).shuffled().forEach { y ->
            (0 until TOTAL_MAP_WIDTH).shuffled().forEach { x ->
                if (blockingLayer.getCell(x, y) == null) {
                    return Vector2(x * TILE_SIZE.toFloat(), y * TILE_SIZE.toFloat())
                }
            }
        }
        throw Exception("Couldn't find an available position")
    }
}

fun TiledMapTile?.isBlocking(): Boolean {
    return this != null && properties.get(BLOCKING) as? Boolean == true
}