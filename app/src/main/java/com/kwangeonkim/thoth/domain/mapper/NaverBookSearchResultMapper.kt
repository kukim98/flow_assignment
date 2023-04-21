package com.kwangeonkim.thoth.domain.mapper

import com.kwangeonkim.thoth.data.model.NaverBookSearchResultDto
import com.kwangeonkim.thoth.domain.model.NaverBook

class NaverBookSearchResultMapper : EntityMapper<List<NaverBook>, NaverBookSearchResultDto> {
    override fun toUiModel(dataModel: NaverBookSearchResultDto): List<NaverBook> {
        return dataModel.items.map {
            NaverBook(
                title = it.title,
                link = it.link,
                image = it.image,
                author = it.author,
                price = if (it.discount == "0") null else it.discount,
                publisher = it.publisher
            )
        }
    }

    // Not Used
    override fun toDataModel(uiModel: List<NaverBook>): NaverBookSearchResultDto {
        return NaverBookSearchResultDto(
            "",
            0, 0, 0, emptyList()
        )
    }
}