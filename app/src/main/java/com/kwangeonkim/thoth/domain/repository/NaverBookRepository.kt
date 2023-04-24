package com.kwangeonkim.thoth.domain.repository

import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.presentation.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NaverBookRepository {

    fun getBooks(): StateFlow<List<NaverBook>>

    suspend fun searchBooks(text: String): Boolean

    fun getTopTenRecentSearchTexts(): Flow<List<String>>
}