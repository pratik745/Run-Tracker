package com.pratik.core.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

import kotlin.time.TimeSource

object Timer {

    fun timeAndEmit(): Flow<Duration> {
        return flow {
            var lastMark = TimeSource.Monotonic.markNow()
            while (true) {
                delay(200L)
                val elapsed = lastMark.elapsedNow()
                emit(elapsed)
                lastMark = TimeSource.Monotonic.markNow()
            }
        }
    }
}