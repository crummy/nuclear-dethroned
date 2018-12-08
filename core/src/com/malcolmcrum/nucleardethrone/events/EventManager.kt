package com.malcolmcrum.nucleardethrone.events

import com.malcolmcrum.nucleardethrone.Log
import kotlin.reflect.KClass

interface EventListener<T : Event> {
    fun handle(event: T)
}

// Is there a better way to do this, without reflection? I haven't figured it out.
class EventManager{
    private val log = Log(EventManager::class)
    private val listeners: MutableMap<KClass<out Event>, MutableList<EventListener<Event>>> = HashMap()

    inline fun <reified T : Event> register(listener: EventListener<T>) {
        register(T::class, listener)
    }

    fun <T : Event> register(eventClass: KClass<out T>, listener: EventListener<T>) {
        val eventListeners = listeners.getOrPut(eventClass) { ArrayList() }
        @Suppress("UNCHECKED_CAST") // a <T : Event> is always going to fit <Event>. not sure why I need this
        eventListeners.add(listener as EventListener<Event>)
    }

    fun notify(event: Event) {
        log.debug("event triggered: $event")
        listeners[event::class]?.forEach { it.handle(event) }
    }
}