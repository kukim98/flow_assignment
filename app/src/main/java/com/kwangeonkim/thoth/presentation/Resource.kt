package com.kwangeonkim.thoth.presentation

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    data class Success<T>(val data: T?): Resource<T>()
    data class Failure(val message: String): Resource<Nothing>()
}