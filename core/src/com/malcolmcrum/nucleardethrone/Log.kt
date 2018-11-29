package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.Gdx


class Log(private val clazz: Class<*>) {
    fun info(message: String) {
        Gdx.app.log(clazz.simpleName, message)
    }
}