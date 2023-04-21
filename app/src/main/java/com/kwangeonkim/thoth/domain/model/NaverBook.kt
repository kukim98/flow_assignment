package com.kwangeonkim.thoth.domain.model


/**
 * Naver book entity
 *
 * @property title
 * @property link the web URL that links to the detail page of the book
 * @property image the image URL for the book
 * @property author
 * @property price
 * @property publisher
 */
data class NaverBook(
    val title: String,
    val link: String,
    val image: String,
    val author: String,
    val price: String?,
    val publisher: String
)