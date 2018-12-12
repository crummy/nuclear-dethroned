package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
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
        player = Player(level.playerStart, this::collisionModifier)
        camera = Camera()
        stage = Stage(camera.viewport)
        stage.addActor(camera)
        enemies.add(Bandit(map.availablePosition(), this::collides))
        enemies.add(Bandit(map.availablePosition(), this::collides))
        enemies.add(Bandit(map.availablePosition(), this::collides))
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

    private fun collisionModifier(velocity: Vector2): Vector2 {
        var x = 1f
        if (velocity.x > 0) {
            if (collidesEast()) x = 0f
        } else if (velocity.x < 0) {
            if (collidesWest()) x = 0f
        }
        var y = 1f
        if (velocity.y > 0) {
            if (collidesNorth()) y = 0f
        } else if (velocity.y < 0) {
            if (collidesSouth()) y = 0f
        }
        return Vector2(x, y)
    }

    // TODO: figure out off-by-one error here
    // TODO: use rectangle.collides
    fun collidesEast(): Boolean {
        return (-.5f..0.5f step 0.5f).any { step ->
            collides(player.position.x + 1, player.position.y + step)
        }
    }

    fun collidesWest(): Boolean {
        return (-.5f..0.5f step 0.5f).any { step ->
            collides(player.position.x, player.position.y + step)
        }
    }

    fun collidesNorth(): Boolean {
        return (-.5f..0.5f step 0.5f).any { step ->
            collides(player.position.x + step, player.position.y + 1)
        }
    }

    fun collidesSouth(): Boolean {
        return (-.5f..0.5f step 0.5f).any { step ->
            collides(player.position.x + step, player.position.y)
        }
    }

    private fun collides(x: Float, y: Float): Boolean {
        return map.tileAt(x, y).isBlocking()
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


}
