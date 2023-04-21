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
) {

    companion object {
        fun sample() = NaverBookDto(
            title = "How to Raise Chickens (Everything You Need to Know)",
            link = "https://search.shopping.naver.com/book/catalog/32445556675",
            image = "https://shopping-phinf.pstatic.net/main_3244555/32445556675.20220518235446.jpg",
            author = "",
            discount = "26780",
            publisher = "Motorbooks Intl",
            isbn = "9780760328286",
            description = "Whichever comes first for you, the chicken or the egg, this book shows you what to do next. In this hands-on, easy-to-use guidebook, longtime chicken breeder and poultry expert Christine Heinrichs tells you everything you need to know to raise chickens in your backyard?from laying out the yard and designing a coop to choosing breeds, caring for chicks, egging, sexing, and butchering.",
            pubdate = "20070315"
        )
    }
}