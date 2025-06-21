package com.wsr

import kotlinx.serialization.Serializable

internal sealed interface AssistantRoute {
    @Serializable
    data object Chat : AssistantRoute

    @Serializable
    data object Setting : AssistantRoute
}
