package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.malcolmcrum.nucleardethrone.actors.Bandit
import com.malcolmcrum.nucleardethrone.actors.BulletManager
import com.malcolmcrum.nucleardethrone.actors.Camera
import com.malcolmcrum.nucleardethrone.actors.Player
import com.malcolmcrum.nucleardethrone.ai.AIDirector
import com.malcolmcrum.nucleardethrone.events.EventManager

val EVENTS = EventManager()

class Game : ApplicationAdapter() {
    val inputHandler = InputHandler()
    val collisionDebugger = CollisionDebugger()
    lateinit var player: Player
    lateinit var camera: Camera
    lateinit var map: DesertMap
    lateinit var mapRenderer: MapRenderer
    lateinit var bulletManager: BulletManager
    val enemies = mutableListOf<Bandit>()
    lateinit var aiDirector: AIDirector

    override fun create() {
        val level = LevelGenerator(64, 64).generate()
        map = DesertMap(level)
        val collisionManager = CollisionManager(map)
        player = Player(map.playerStart, collisionManager::checkWallCollision)
        camera = Camera()
        enemies.add(Bandit(map.availablePosition()))
        enemies.add(Bandit(map.availablePosition()))
        enemies.add(Bandit(map.availablePosition()))
        bulletManager = BulletManager(collisionManager::checkWallCollision)
        mapRenderer = OrthogonalTiledMapRenderer(map.map, 1/8f)
        mapRenderer.setView(camera.viewport.camera as OrthographicCamera)
        aiDirector = AIDirector(enemies, map) { player.position }
        Gdx.input.isCursorCatched = true
    }

    override fun render() {
        inputHandler.handle(camera.viewport.camera)
        aiDirector.update()
        enemies.forEach(Bandit::update)
        Gdx.gl.glClearColor(170/255f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        mapRenderer.setView(camera.viewport.camera as OrthographicCamera)
        mapRenderer.render(listOf(1, 2, 3, 4, 5).toIntArray()) // render walls
        val batch = SpriteBatch()
        batch.projectionMatrix = camera.viewport.camera.combined
        batch.begin()
        player.draw(batch)
        bulletManager.draw(batch)
        enemies.forEach { it.draw(batch) }
        batch.end()
        mapRenderer.render(listOf(0).toIntArray()) // render blocking tiles
        batch.begin()
        collisionDebugger.draw(batch)
        batch.end()
        camera.update()
    }

    override fun resize(width: Int, height: Int) {
        camera.viewport.update(width, height, true)
    }


}
