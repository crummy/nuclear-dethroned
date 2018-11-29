package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import kotlin.random.Random

const val BLOCKING: String = "BLOCKING"

class DesertMap {
    val map: TiledMap = TiledMap()
    val cell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val textureRegion = TextureRegion(Texture("tile.png"))
    val blockingTile = StaticTiledMapTile(textureRegion)

    init {
        blockingTile.properties.put(BLOCKING, true)
        cell.tile = blockingTile
        val tileLayer = TiledMapTileLayer(64, 64, 8, 8)
        (0..64).forEach { x ->
            (0..64).forEach { y ->
                if (x == 0 || x == 64 || y == 0 || y == 64) {
                    tileLayer.setCell(x, y, cell)
                }
            }
        }
        map.layers.add(tileLayer)
    }

    val blockingTiles = map.layers[0].objects.filter { it.properties[BLOCKING] as Boolean }
}