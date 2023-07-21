package com.congvtt1.smartmovie.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel<S : Any> : ViewModel() {
    val store by lazy {
        ViewStateStore(this.initState())
    }
    val currentState: S
        get() = store.state

    private val _errorLiveEvent: MutableLiveData<Throwable> = MutableLiveData<Throwable>()

    val errorLiveEvent: LiveData<Throwable>
        get() = _errorLiveEvent

    protected val handlerException = CoroutineExceptionHandler { _, exception ->
        dispatchError(exception)
    }

    private fun dispatchError(error: Throwable) {
        _errorLiveEvent.value = error
    }

    protected fun dispatchState(state: S) {
        store.dispatchState(state = state)
    }

    abstract fun initState(): S
}
