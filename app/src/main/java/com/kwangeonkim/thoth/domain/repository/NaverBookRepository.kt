package com.kwangeonkim.thoth.domain.repository

import com.kwangeonkim.thoth.domain.model.NaverBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NaverBookRepository {

    fun getBooks(): StateFlow<List<NaverBook>>

    suspend fun searchBooks(text: String)

    fun getTopTenRecentSearchTexts(): Flow<List<String>>
}