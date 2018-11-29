package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.malcolmcrum.nucleardethrone.*

class Player(val crosshair: Crosshair) : Actor() {
    val log = Log(Player::class.java)
    val texture = Texture("player.png")

    init {
        setBoundsCentered(0f, 0f, 1024f, 768f)
        addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
            }

            override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.W -> y += 1
                    Input.Keys.S -> y -= 1
                    Input.Keys.D -> x += 1
                    Input.Keys.A -> x -= 1
                }
                return true
            }
        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.drawCentered(texture, x, y)
        crosshair.draw(batch, parentAlpha)
    }

    fun handleInput(input: Inputs) {
        when (input) {
            is MoveX -> this.x = input.x
            is MoveY -> this.y = input.y
            is Aim -> crosshair.setPosition(input.x, input.y)
        }
    }
}