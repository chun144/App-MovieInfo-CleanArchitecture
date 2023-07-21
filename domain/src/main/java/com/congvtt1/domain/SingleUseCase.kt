package com.congvtt1.domain

interface SingleUseCase<T> {
    suspend fun execute():T
}