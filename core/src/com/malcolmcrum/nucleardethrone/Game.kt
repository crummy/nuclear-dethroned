package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.malcolmcrum.nucleardethrone.actors.Crosshair
import com.malcolmcrum.nucleardethrone.actors.Player
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer



class Game : ApplicationAdapter() {
    lateinit var camera: OrthographicCamera
    lateinit var stage: Stage
    lateinit var player: Player
    val viewport = FitViewport(160f, 100f)
    lateinit var map: DesertMap
    lateinit var mapRenderer: MapRenderer

    override fun create() {
        map = DesertMap()
        camera = OrthographicCamera()
        camera.setToOrtho(false, 1024f, 768f)
        stage = Stage(viewport)
        val crosshair = Crosshair()
        player = Player(crosshair)
        stage.addActor(player)
        stage.addActor(crosshair)
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0f)
        mapRenderer = OrthogonalTiledMapRenderer(map.map)
        mapRenderer.setView(viewport.camera as OrthographicCamera?)
    }

    override fun render() {
        camera.update()
        getInputs(viewport).forEach { input ->
            player.handleInput(input)
        }
        stage.isDebugAll = true
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
        mapRenderer.render()

    }

    override fun dispose() {
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


}
