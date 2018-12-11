package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.malcolmcrum.nucleardethrone.events.*

class InputHandler {
    private val lastMouseDown: MutableMap<Int, Boolean> = HashMap()

    fun handle(camera: Camera) {
        handleKeyboardInput()
        handleMouseInput(camera)
    }

    private fun handleKeyboardInput() {
        var y = 0
        if (Gdx.input.isKeyPressed(W)) {
            y = 1
        }
        if (Gdx.input.isKeyPressed(S)) {
            y = -1
        }
        var x = 0
        if (Gdx.input.isKeyPressed(A)) {
            x = -1
        }
        if (Gdx.input.isKeyPressed(D)) {
            x = 1
        }
        EVENTS.notify(PlayerMovement(x, y))
    }

    private fun handleMouseInput(camera: Camera) {
        val aimPosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        camera.unproject(aimPosition)
        EVENTS.notify(MouseAimed(aimPosition.x, aimPosition.y))

        val mouseButtonDown = Gdx.input.isButtonPressed(LEFT)
        if (mouseButtonDown) {
            EVENTS.notify(MouseDragged(aimPosition.x, aimPosition.y))
            if (lastMouseDown[LEFT] == false) {
                EVENTS.notify(MouseDown(aimPosition.x, aimPosition.y))
            }
        } else {
            if (lastMouseDown[LEFT] == true) {
                EVENTS.notify(MouseUp(aimPosition.x, aimPosition.y))
            }
        }
        lastMouseDown[LEFT] = mouseButtonDown
    }
}