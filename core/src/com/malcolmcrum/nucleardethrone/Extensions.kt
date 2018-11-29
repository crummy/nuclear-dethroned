package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import org.w3c.dom.Text

fun Batch.drawCentered(texture: Texture, x: Float, y: Float) {
    this.draw(texture, x - texture.width / 2, y - texture.height / 2)
}

fun Actor.setBoundsCentered(x: Float, y: Float, width: Float, height: Float) {
    this.setBounds(x - width/2, y  - height/2, width, height)
}