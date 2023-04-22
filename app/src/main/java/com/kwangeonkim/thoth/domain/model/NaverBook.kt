package com.kwangeonkim.thoth.domain.model

import com.kwangeonkim.thoth.data.model.NaverBookDto


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
) {

    companion object {
        fun sample() = NaverBook(
            title = "How to Raise Chickens (Everything You Need to Know)",
            link = "https://search.shopping.naver.com/book/catalog/32445556675",
            image = "https://shopping-phinf.pstatic.net/main_3244555/32445556675.20220518235446.jpg",
            author = "",
            price = "26780",
            publisher = "Motorbooks Intl"
        )
    }
}