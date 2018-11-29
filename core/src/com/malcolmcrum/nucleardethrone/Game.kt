package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import javax.swing.Spring.height
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.malcolmcrum.nucleardethrone.actors.Player

class Game : ApplicationAdapter() {
    lateinit var camera: OrthographicCamera
    lateinit var stage: Stage

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, 1024f, 768f)
        val width = 1024
        val height = 768
        stage = Stage(StretchViewport(width.toFloat(), height.toFloat()))
        val player = Player()
        player.touchable = Touchable.enabled
        stage.addActor(player)
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
