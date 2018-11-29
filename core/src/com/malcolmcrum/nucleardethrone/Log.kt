package com.malcolmcrum.nucleardethrone

import com.badlogic.gdx.Gdx
import kotlin.reflect.KClass



class Log(private val kClass: KClass<Any>) {
    fun info(message: String) {
        Gdx.app.log(kClass.simpleName, message)
    }
}