package com.kwangeonkim.thoth.data.local.naver.dao

import androidx.room.*
import com.kwangeonkim.thoth.data.local.naver.dto.NaverBookSearchTextDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NaverBookSearchDao {
    @Query("SELECT * FROM search_texts ORDER BY timestamp DESC LIMIT 10")
    fun getAllSearchTexts(): Flow<List<NaverBookSearchTextDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchText(naverBookSearchTextDto: NaverBookSearchTextDto)
}