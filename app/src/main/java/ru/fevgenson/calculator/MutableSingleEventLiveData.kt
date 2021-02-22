package ru.fevgenson.calculator

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface SingleEventLiveData<T> {

    fun observe(lifecycleOwner: LifecycleOwner, eventListener: (T) -> Unit)
}

class MutableSingleEventLiveData<T> : LifecycleObserver, SingleEventLiveData<T> {

    private var mainListener: ((T) -> Unit)? = null
    private var eventListener: ((T) -> Unit)? = null
    private val events = mutableListOf<T>()

    override fun observe(lifecycleOwner: LifecycleOwner, eventListener: (T) -> Unit) {
        mainListener = eventListener
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectEventListener() {
        eventListener = mainListener
        eventListener?.let { listener ->
            events.forEach { event ->
                listener(event)
            }
            events.clear()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectEventListener() {
        eventListener = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() {
        eventListener = null
        mainListener = null
    }

    fun dispatchEvent(event: T) {
        eventListener?.invoke(event) ?: events.add(event)
    }
}