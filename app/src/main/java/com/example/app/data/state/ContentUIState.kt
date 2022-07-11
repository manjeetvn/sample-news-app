package com.example.app.data.state

/**
 * UI States defined for StateFlow in the workflow
 * Generic Type - Can be Used with Any Model
 */
sealed class ContentUIState<out T: Any> {

    // List of results coming from the Data layer
    data class SuccessUIState<out T: Any>(val content: T): ContentUIState<T>()

    // Error state with error message
    data class ErrorUIState(val msg: String): ContentUIState<Nothing>()

    // Empty state with "no results" String
    object EmptyUIState: ContentUIState<Nothing>()

    // Loading state with a pop up or dialog
    object LoadingUIState: ContentUIState<Nothing>()
}