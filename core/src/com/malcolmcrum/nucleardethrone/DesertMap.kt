package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile

const val BLOCKING: String = "BLOCKING"

class DesertMap {
    val map: TiledMap = TiledMap()
    val cell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val textureRegion = TextureRegion(Texture("tile.png"))
    val blockingTile = StaticTiledMapTile(textureRegion)

    init {
        blockingTile.properties.put(BLOCKING, true)
        cell.tile = blockingTile
        val tileLayer = TiledMapTileLayer(32, 32, 8, 8)
        (0..32).forEach { x ->
            (0..32).forEach { y ->
                if (x == 0 || x == 31 || y == 0 || y == 31) {
                    tileLayer.setCell(x, y, cell)
                }
            }
        }
        map.layers.add(tileLayer)
    }

    val blockingTiles = map.layers[0].objects.filter { it.properties[BLOCKING] as Boolean }
}