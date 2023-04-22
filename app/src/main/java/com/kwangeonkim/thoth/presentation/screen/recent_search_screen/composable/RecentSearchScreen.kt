package com.kwangeonkim.thoth.presentation.screen.recent_search_screen.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kwangeonkim.thoth.data.repository.fake_repository.FakeNaverBookRepositoryImpl
import com.kwangeonkim.thoth.domain.mapper.NaverBookSearchResultMapper
import com.kwangeonkim.thoth.presentation.screen.recent_search_screen.RecentSearchEvent
import com.kwangeonkim.thoth.presentation.screen.recent_search_screen.RecentSearchUiEvent
import com.kwangeonkim.thoth.presentation.screen.recent_search_screen.RecentSearchViewModel
import com.kwangeonkim.thoth.presentation.theme.ThothTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentSearchScreen(
    navController: NavController, viewModel: RecentSearchViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is RecentSearchUiEvent.MoveBackToRecentSearchesScreenEvent -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set("text", it.text)
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text(text = "최근검색") }) }) {

        LazyColumn(modifier = Modifier
            .padding(it)
            .padding(16.dp)
            .fillMaxWidth(), content = {
            items(state.recentSearchList.size) {
                val text = state.recentSearchList[it]
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            viewModel.fireEvent(RecentSearchEvent.SearchTextTappedEvent(text))
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = text, overflow = TextOverflow.Ellipsis
                    )
                }
            }
        })
    }
}

@Preview
@Composable
fun RecentSearchScreenPreview() {
    ThothTheme {
        RecentSearchScreen(
            navController = rememberNavController(),
            viewModel = RecentSearchViewModel(FakeNaverBookRepositoryImpl(
                NaverBookSearchResultMapper()
            ).apply {
                searchTexts.put("police", "null")
                searchTexts.put("police1", "null")
                searchTexts.put("police2", "null")
                searchTexts.put("police3", "null")
                searchTexts.put("police4", "null")
                searchTexts.put("police5", "null")
                searchTexts.put("police6", "null")
                searchTexts.put("police7", "null")
                searchTexts.put("police8", "null")
                searchTexts.put("police9", "null")
                searchTexts.put("police10", "null")
            })
        )
    }
}