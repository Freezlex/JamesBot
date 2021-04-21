package com.freezlex.jamesbot.internals.event

import com.freezlex.jamesbot.internals.commands.Command
import com.freezlex.jamesbot.internals.event.listener.OnMessageReceivedListener
import com.freezlex.jamesbot.internals.event.listener.OnReadyListener
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * The Event registry to manage all the event listeners
 */
@Component
class EventRegistry @Autowired constructor(
    val onMessageReceivedEvent: OnMessageReceivedListener,
    val onReadyListener: OnReadyListener
): ListenerAdapter() {

    private lateinit var listeners: List<DefaultListener>

    /**
     * Method to register all listeners into the listenerRegistry
     */
    fun loadListeners(){
        listeners = listOf(
            onMessageReceivedEvent,
            onReadyListener
        )
    }

    /**
     * Get all the listeners registered in the ListenersRegistry
     * @return The listeners list contained in the ListenersRegistry
     */
    fun build(): List<DefaultListener> = listeners;
}
