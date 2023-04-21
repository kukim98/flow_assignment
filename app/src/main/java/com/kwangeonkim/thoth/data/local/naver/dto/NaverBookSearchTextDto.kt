package com.kwangeonkim.thoth.data.local.naver.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_texts")
data class NaverBookSearchTextDto(
    @PrimaryKey val text: String,
    @ColumnInfo val timestamp: Long
) {
    constructor(text: String) : this(text, System.currentTimeMillis())
}