package com.kwangeonkim.thoth.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for book search result with Naver's API
 *
 * @property lastBuildDate the time it took to generate the serach
 * @property total the total number of searched items
 * @property start the offset to start hte search
 * @property display the number of returned searched items
 * @property items the list of book information
 */
data class NaverBookSearchResultDto(
    @SerializedName("lastBuildDate") val lastBuildDate: String,
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("display") val display: Int,
    @SerializedName("items") val items: List<NaverBookDto>
)