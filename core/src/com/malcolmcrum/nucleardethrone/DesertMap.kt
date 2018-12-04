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
    val cell: TiledMapTileLayer.Cell = TiledMapTileLayer.Cell()
    val textureRegion = TextureRegion(Texture("tile.png"))
    val blockingTile = StaticTiledMapTile(textureRegion)

    init {
        blockingTile.properties.put(BLOCKING, true)
        cell.tile = blockingTile
        val tileLayer = TiledMapTileLayer(32, 32, 8, 8)
        (0..32).forEach { x ->
            (0..32).forEach { y ->
                if (x == 0 || x == 31 || y == 0 || y == 31 || Random.nextInt(10) < 2) {
                    tileLayer.setCell(x, y, cell)
                }
            }
        }
        map.layers.add(tileLayer)
    }

    fun tileAt(x: Float, y: Float): TiledMapTile? {
        val layer: TiledMapTileLayer = map.layers[0] as TiledMapTileLayer
        return layer.getCell((x / TILE_SIZE).toInt(), (y / TILE_SIZE).toInt())?.tile
    }


}

fun TiledMapTile?.isBlocking(): Boolean {
    return this != null && properties.get(BLOCKING) as? Boolean == true
}