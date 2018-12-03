package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.malcolmcrum.nucleardethrone.actors.Camera
import com.malcolmcrum.nucleardethrone.actors.Crosshair
import com.malcolmcrum.nucleardethrone.actors.Player


class Game : ApplicationAdapter() {
    lateinit var stage: Stage
    lateinit var player: Player
    lateinit var camera: Camera
    lateinit var map: DesertMap
    lateinit var mapRenderer: MapRenderer

    override fun create() {
        map = DesertMap()
        val crosshair = Crosshair()
        player = Player(crosshair)
        camera = Camera(player, crosshair)
        stage = Stage(camera.viewport)
        stage.addActor(player)
        stage.addActor(crosshair)
        stage.addActor(camera)
        mapRenderer = OrthogonalTiledMapRenderer(map.map)
        mapRenderer.setView(camera.viewport.camera as OrthographicCamera)
    }

    override fun render() {
        getInputs(camera.viewport).forEach { input ->
            player.handleInput(input) { bounds, velocity -> checkCollision(bounds, velocity) }
        }
        stage.isDebugAll = true
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
        mapRenderer.setView(camera.viewport.camera as OrthographicCamera)
        mapRenderer.render()
        camera.update()
    }

    private fun checkCollision(inBounds: Rectangle, velocity: Vector2): Boolean {
        val bounds = Rectangle(inBounds.x / 8, inBounds.y / 8, 1f, 1f)
        val layer: TiledMapTileLayer = map.map.layers[0] as TiledMapTileLayer
        if (velocity.x > 0) {
            val lowerCell: TiledMapTileLayer.Cell? = layer.getCell((bounds.x + bounds.width).toInt(), bounds.y.toInt())
            val upperCell: TiledMapTileLayer.Cell? = layer.getCell((bounds.x + bounds.width).toInt(), (bounds.y + bounds.height).toInt())
            if (lowerCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
            if (upperCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
        } else if (velocity.x < 0) {
            val lowerCell: TiledMapTileLayer.Cell? = layer.getCell(bounds.x.toInt(), bounds.y.toInt())
            val upperCell: TiledMapTileLayer.Cell? = layer.getCell(bounds.x.toInt(), (bounds.y + bounds.height).toInt())
            if (lowerCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
            if (upperCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
        }
        if (velocity.y > 0) {
            val leftCell: TiledMapTileLayer.Cell? = layer.getCell((bounds.x + bounds.width).toInt(), (bounds.y + bounds.height).toInt())
            val rightCell: TiledMapTileLayer.Cell? = layer.getCell(bounds.x.toInt(), (bounds.y + bounds.height).toInt())
            if (leftCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
            if (rightCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
        } else if (velocity.y < 0) {
            val leftCell: TiledMapTileLayer.Cell? = layer.getCell((bounds.x + bounds.width).toInt(), bounds.y.toInt())
            val rightCell: TiledMapTileLayer.Cell? = layer.getCell(bounds.x.toInt(), bounds.y.toInt())
            if (leftCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
            if (rightCell?.tile?.properties?.get(BLOCKING) as? Boolean == true) {
                return true
            }
        }
        return false
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


}
