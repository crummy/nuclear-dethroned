package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.malcolmcrum.nucleardethrone.actors.Crosshair
import com.malcolmcrum.nucleardethrone.actors.Player

class Game : ApplicationAdapter() {
    lateinit var camera: OrthographicCamera
    lateinit var stage: Stage

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, 1024f, 768f)
        val width = 1024
        val height = 768
        stage = Stage(FitViewport(320f, 200f))
        val crosshair = Crosshair()
        crosshair.touchable = Touchable.enabled
        val player = Player(crosshair)
        player.touchable = Touchable.enabled
        stage.addActor(player)
        stage.addActor(crosshair)
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0f)
        Gdx.input.inputProcessor = stage
    }

    override fun render() {
        camera.update()
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


}
