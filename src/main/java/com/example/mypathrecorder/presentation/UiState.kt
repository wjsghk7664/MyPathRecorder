package com.example.mypathrecorder.presentation

sealed interface UiState<out T> {
    data object Init:UiState<Nothing>
    data object Loading:UiState<Nothing>
    data class Failure(val e:String): UiState<Nothing>
    data class Success<T>(val data: T):UiState<T>
}