package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.malcolmcrum.nucleardethrone.actors.Bandit
import com.malcolmcrum.nucleardethrone.actors.BulletManager
import com.malcolmcrum.nucleardethrone.actors.Camera
import com.malcolmcrum.nucleardethrone.actors.Player
import com.malcolmcrum.nucleardethrone.events.EventManager

val EVENTS = EventManager()

class Game : ApplicationAdapter() {
    val inputHandler = InputHandler()
    lateinit var stage: Stage
    lateinit var player: Player
    lateinit var camera: Camera
    lateinit var map: DesertMap
    lateinit var mapRenderer: MapRenderer
    lateinit var bulletManager: BulletManager
    val enemies = mutableListOf<Bandit>()

    override fun create() {
        val level = LevelGenerator(64, 64).generate()
        map = DesertMap(level)
        val collisionManager = CollisionManager(map)
        player = Player(map.playerStart, collisionManager::checkCollision)
        camera = Camera()
        stage = Stage(camera.viewport)
        stage.addActor(camera)
        enemies.add(Bandit(map.availablePosition()))
        enemies.add(Bandit(map.availablePosition()))
        enemies.add(Bandit(map.availablePosition()))
        bulletManager = BulletManager()
        mapRenderer = OrthogonalTiledMapRenderer(map.map, 1/8f)
        mapRenderer.setView(camera.viewport.camera as OrthographicCamera)
        Gdx.input.isCursorCatched = true
    }

    override fun render() {
        inputHandler.handle(camera.viewport.camera)
        enemies.forEach(Bandit::update)
        Gdx.gl.glClearColor(170/255f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.isDebugAll = true
        stage.act(Gdx.graphics.deltaTime)
        mapRenderer.setView(camera.viewport.camera as OrthographicCamera)
        mapRenderer.render(listOf(1).toIntArray()) // render walls
        stage.draw() // render player, etc
        stage.batch.begin()
        player.draw(stage.batch)
        bulletManager.draw(stage.batch)
        enemies.forEach { it.draw(stage.batch) }
        stage.batch.end()
        mapRenderer.render(listOf(0).toIntArray()) // render blocking tiles
        camera.update()
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


}
