package com.congvtt1.domain

interface SingleUseCaseWithParameter<P, R> {
    suspend fun execute(parameter: P): R
}