package com.kwangeonkim.thoth.data.model

/**
 * DTO for book info from search with Naver's API
 *
 * @property title
 * @property link the web URL that links to the detail page of the book
 * @property image the image URL for the thumbnail of the book
 * @property author
 * @property discount the sales price; will be "0" if not for sale
 * @property publisher
 * @property isbn
 * @property description the description of the book
 * @property pubdate the publication date in YYYYmmDD format
 */
data class NaverBookDto(
    val title: String,
    val link: String,
    val image: String,
    val author: String,
    val discount: String,
    val publisher: String,
    val isbn: String,
    val description: String,
    val pubdate: String
)