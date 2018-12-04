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
        setPosition(100f, 100f)
        setBounds(100f, 100f, texture.width.toFloat(), texture.height.toFloat())
        addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
            }

            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                log.info("mouse moved: $x, $y")
                moveBy(x, y)
                //setBoundsCentered(x, y, texture.width.toFloat(), texture.height.toFloat())
                return true
            }
        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y)
    }
}