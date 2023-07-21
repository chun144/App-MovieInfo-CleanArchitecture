package com.congvtt1.smartmovie.base.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map

class ViewStateStore<T : Any>(
    initialState: T
) {
    private val stateLiveData = MutableLiveData<T>().apply {
        value = initialState
    }

    val state: T
        get() = stateLiveData.value!!

    fun <S> observe(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        stateLiveData.map(selector)
            .distinctUntilChanged()
            .observe(owner, observer)
    }

    fun <S> observeAnyway(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        stateLiveData.map(selector)
            .observe(owner, observer)
    }

    fun dispatchState(state: T) {
        stateLiveData.value = state
    }
}