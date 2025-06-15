package com.wsr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect

abstract class Presenter<State : UiState, Event : UiEvent>(initialValue: State) {
    protected val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    var uiState by mutableStateOf(initialValue)
        protected set

    private val _uiEvent = MutableSharedFlow<Event>()
    val uiEvent = _uiEvent.asSharedFlow()
    suspend fun emitEvent(event: Event) = _uiEvent.emit(event)
}

@Composable
fun <T : UiEvent> ObserveEvent(presenter: Presenter<*, T>, block: (T) -> Unit) {
    LaunchedEffect(presenter) {
        presenter.uiEvent.collect(block)
    }
}

interface UiState

interface UiEvent
