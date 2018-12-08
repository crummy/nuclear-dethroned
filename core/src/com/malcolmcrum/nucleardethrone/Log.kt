package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.Gdx
import kotlin.reflect.KClass


class Log(private val clazz: KClass<*>) {
    fun info(message: String) {
        Gdx.app.log("INFO ${clazz::java}", message)
    }

    fun debug(message: String) {
        Gdx.app.log("DEBUG ${clazz::java}", message)
    }
}