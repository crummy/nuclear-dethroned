package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.Gdx
import kotlin.reflect.KClass


class Log(private val clazz: KClass<*>) {
    val debug = false

    fun info(message: String) {
        Gdx.app.log("INFO ${clazz::java}", message)
    }

    fun debug(message: String) {
        if (debug) {
            Gdx.app.log("DEBUG ${clazz::java}", message)
        }
    }
}