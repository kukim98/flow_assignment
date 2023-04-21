package com.kwangeonkim.thoth.data.local.naver

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kwangeonkim.thoth.data.local.naver.dao.NaverBookSearchDao
import com.kwangeonkim.thoth.data.local.naver.dto.NaverBookSearchTextDto

@Database(entities = [NaverBookSearchTextDto::class], version = 1)
abstract class NaverBookDatabase: RoomDatabase() {

    abstract val naverBookSearchDao: NaverBookSearchDao

    companion object {
        const val DATABASE_NAME = "naver_db"
    }
}