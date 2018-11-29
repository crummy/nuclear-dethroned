package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import kotlin.random.Random

class DesertMap {
    val map: TiledMap = TiledMap()
    val cell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val textureRegion = TextureRegion(Texture("tile.png"))

    init {
        cell.tile = StaticTiledMapTile(textureRegion)
        val tileLayer = TiledMapTileLayer(64, 64, 8, 8)
        (0..64).forEach { x ->
            (0..64).forEach { y ->
                if (Random.nextInt(10) < 3) {
                    tileLayer.setCell(x, y, cell)
                }
            }
        }
        map.layers.add(tileLayer)
    }
}