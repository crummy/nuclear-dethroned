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
import com.malcolmcrum.nucleardethrone.actors.Camera
import com.malcolmcrum.nucleardethrone.actors.Crosshair
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
    val enemies = mutableListOf<Bandit>()

    override fun create() {
        map = DesertMap()
        val crosshair = Crosshair()
        player = Player(this::collisionModifier)
        camera = Camera()
        stage = Stage(camera.viewport)
        stage.addActor(crosshair)
        stage.addActor(player)
        stage.addActor(camera)
        enemies.add(Bandit(this::collides))
        enemies.add(Bandit(this::collides))
        enemies.add(Bandit(this::collides))
        enemies.forEach(stage::addActor)
        mapRenderer = OrthogonalTiledMapRenderer(map.map)
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
    fun collidesEast(): Boolean {
        return (1f..(player.height) step TILE_SIZE / 2f).any { step ->
            collides(player.x + player.width, player.y + step)
        }
    }

    fun collidesWest(): Boolean {
        return (1f..(player.height) step TILE_SIZE / 2f).any { step ->
            collides(player.x, player.y + step)
        }
    }

    fun collidesNorth(): Boolean {
        return (1f..(player.width) step TILE_SIZE / 2f).any { step ->
            collides(player.x + step, player.y + player.height)
        }
    }

    fun collidesSouth(): Boolean {
        return (1f..(player.width) step TILE_SIZE / 2f).any { step ->
            collides(player.x + step, player.y)
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
