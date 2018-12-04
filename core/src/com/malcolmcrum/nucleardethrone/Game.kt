package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
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
            player.handleInput(input, this::checkCollision)
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

    private fun checkCollision(velocity: Vector2): Boolean {
        if (velocity.x > 0) {
            if (collidesEast()) return true
        } else if (velocity.x < 0) {
            if (collidesWest()) return true
        }
        if (velocity.y > 0) {
            if (collidesNorth()) return true
        } else if (velocity.y < 0) {
            if (collidesSouth()) return true
        }
        return false
    }

    fun collidesEast(): Boolean {
        return (0f..player.height step TILE_SIZE / 2f).any { step ->
            collides(player.x + player.width, player.y + step)
        }
    }

    fun collidesWest(): Boolean {
        return (0f..player.height step TILE_SIZE / 2f).any { step ->
            collides(player.x, player.y + step)
        }
    }

    fun collidesNorth(): Boolean {
        return (0f..player.width step TILE_SIZE / 2f).any { step ->
            collides(player.x + step, player.y + player.height)
        }
    }

    fun collidesSouth(): Boolean {
        return (0f..player.width step TILE_SIZE / 2f).any { step ->
            collides(player.x + step, player.y)
        }
    }

    private fun collides(x: Float, y: Float): Boolean {
        val layer: TiledMapTileLayer = map.map.layers[0] as TiledMapTileLayer
        return layer.getCell((x / TILE_SIZE).toInt(), (y / TILE_SIZE).toInt())?.tile?.properties?.get(BLOCKING) as? Boolean == true
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


}
