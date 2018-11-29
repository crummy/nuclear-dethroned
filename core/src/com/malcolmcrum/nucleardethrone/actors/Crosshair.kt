package com.malcolmcrum.nucleardethrone.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.malcolmcrum.nucleardethrone.Log

class Crosshair : Actor() {
    val log = Log(Crosshair::class.java)
    val texture = Texture("crosshair.png")

    init {
        setBounds(0f, 0f, 1024f, 768f)
        addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                TODO()
            }

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                log.info("mouse moved: $x, $y")
                setX(x)
                setY(y)
                return true
            }
        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }
}