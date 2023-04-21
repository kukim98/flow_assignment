package com.kwangeonkim.thoth.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kwangeonkim.thoth.data.local.naver.NaverBookDatabase
import com.kwangeonkim.thoth.data.local.naver.dao.NaverBookSearchDao
import com.kwangeonkim.thoth.data.local.naver.dto.NaverBookSearchTextDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NaverLocalCacheTest {

    private lateinit var naverBookSearchDao: NaverBookSearchDao
    private lateinit var db: NaverBookDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, NaverBookDatabase::class.java
        ).build()
        naverBookSearchDao = db.naverBookSearchDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertNewSearchTextWhenEmpty() = runBlocking {

        // Listen to DB changes and validate after entry
        launch {
            val searchHistory = naverBookSearchDao.getAllSearchTexts().take(1).single()
            assertEquals(1, searchHistory.size)
            assertEquals("Fourscore", searchHistory.first().text)
        }
        val text = NaverBookSearchTextDto(text = "Fourscore")
        naverBookSearchDao.insertSearchText(text)
    }

    @Test
    fun insertNewSearchTextWithExistingItems() = runBlocking {

        // Prepare
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("ago"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("years"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("seven"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("and"))

        // Listen to DB changes and validate after entry
        launch {
            val searchHistory = naverBookSearchDao.getAllSearchTexts().take(1).single()
            assertEquals(5, searchHistory.size)
            assertEquals("Fourscore", searchHistory[0].text)
            assertEquals("and", searchHistory[1].text)
            assertEquals("seven", searchHistory[2].text)
            assertEquals("years", searchHistory[3].text)
            assertEquals("ago", searchHistory[4].text)
        }

        val text = NaverBookSearchTextDto(text = "Fourscore")
        naverBookSearchDao.insertSearchText(text)
    }

    @Test
    fun insertExistingSearchTextWithExistingItems() = runBlocking {

        // Prepare
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("Fourscore"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("years"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("seven"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("and"))

        // Listen to DB changes and validate after entry
        launch {
            val searchHistory = naverBookSearchDao.getAllSearchTexts().take(1).single()
            assertEquals(4, searchHistory.size)
            assertEquals("Fourscore", searchHistory[0].text)
            assertEquals("and", searchHistory[1].text)
            assertEquals("seven", searchHistory[2].text)
            assertEquals("years", searchHistory[3].text)
        }

        val text = NaverBookSearchTextDto(text = "Fourscore")
        naverBookSearchDao.insertSearchText(text)
    }

    @Test
    fun insertNewSearchTextWithFullItems() = runBlocking {

        // Prepare
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("replace this"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("on"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("forth"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("brought"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("fathers"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("our"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("ago"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("years"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("seven"))
        delay(10)
        naverBookSearchDao.insertSearchText(NaverBookSearchTextDto("and"))

        // Listen to DB changes and validate after entry
        launch {
            val searchHistory = naverBookSearchDao.getAllSearchTexts().take(1).single()
            assertEquals(10, searchHistory.size)
            assertEquals("Fourscore", searchHistory[0].text)
            assertEquals("and", searchHistory[1].text)
            assertEquals("seven", searchHistory[2].text)
            assertEquals("years", searchHistory[3].text)
            assertEquals("ago", searchHistory[4].text)
            assertEquals("our", searchHistory[5].text)
            assertEquals("fathers", searchHistory[6].text)
            assertEquals("brought", searchHistory[7].text)
            assertEquals("forth", searchHistory[8].text)
            assertEquals("on", searchHistory[9].text)
        }

        val text = NaverBookSearchTextDto(text = "Fourscore")
        naverBookSearchDao.insertSearchText(text)
    }
}