package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import kotlin.random.Random

const val BLOCKING: String = "BLOCKING"
const val TILE_SIZE = 8

class DesertMap {
    val map: TiledMap = TiledMap()
    val texture = Texture("tile.png")
    val blockingCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val wallCell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val blockingTexture = TextureRegion(texture, 8, 8, 8, 8)
    val wallTexture = TextureRegion(texture, 8, 16, 8, 8)
    val blockingTile = StaticTiledMapTile(blockingTexture)
    val wallTile = StaticTiledMapTile(wallTexture)
    val blockingLayer = TiledMapTileLayer(32, 32, TILE_SIZE, TILE_SIZE)
    val wallLayer = TiledMapTileLayer(32, 32, TILE_SIZE, TILE_SIZE)

    init {
        blockingTile.properties.put(BLOCKING, true)
        blockingTile.offsetY = TILE_SIZE / 2f
        wallTile.offsetY = -TILE_SIZE / 2f
        blockingCell.tile = blockingTile
        wallCell.tile = wallTile

        (0..32).forEach { x ->
            (0..32).forEach { y ->
                if (x == 0 || x == 31 || y == 0 || y == 31 || Random.nextInt(10) < 1) {
                    blockingLayer.setCell(x, y, blockingCell)
                    wallLayer.setCell(x, y, wallCell)
                }
            }
        }
        map.layers.add(blockingLayer)
        map.layers.add(wallLayer)
    }

    fun tileAt(x: Float, y: Float): TiledMapTile? {
        return blockingLayer.getCell((x / TILE_SIZE).toInt(), (y / TILE_SIZE).toInt())?.tile
    }
}

fun TiledMapTile?.isBlocking(): Boolean {
    return this != null && properties.get(BLOCKING) as? Boolean == true
}