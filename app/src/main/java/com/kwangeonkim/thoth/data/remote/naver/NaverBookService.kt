package com.kwangeonkim.thoth.data.remote.naver

import com.kwangeonkim.thoth.data.model.NaverBookSearchResultDto
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Naver Book Service API
 */
interface NaverBookService {

    @GET("/v1/search/book.json")
    suspend fun searchBooks(@Query("query") searchText: String): NaverBookSearchResultDto
}