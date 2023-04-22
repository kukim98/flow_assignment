package com.kwangeonkim.thoth.presentation.screen.search_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kwangeonkim.thoth.data.repository.fake_repository.FakeNaverBookRepositoryImpl
import com.kwangeonkim.thoth.domain.mapper.NaverBookSearchResultMapper
import com.kwangeonkim.thoth.presentation.screen.Screen
import com.kwangeonkim.thoth.presentation.theme.ThothTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {

    val state = viewModel.state.value
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is SearchUiEvent.MoveToBookWebPageEvent -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
                }
                is SearchUiEvent.MoveToRecentSearchesScreenEvent -> {
                    navController.navigate(Screen.RecentSearchScreen.route)
                }
                is SearchUiEvent.DismissKeyboardEvent -> {
                    focusManager.clearFocus()
                }
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "책 검색") }, actions = {
            TextButton(onClick = { viewModel.fireEvent(SearchEvent.RecentSearchesButtonTappedEvent) }) {
                Text(text = "최근 검색어")
            }
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.text,
                    onValueChange = {
                        viewModel.fireEvent(
                            SearchEvent.SearchTextFormFieldValueChangedEvent(it)
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions {
                        viewModel.fireEvent(SearchEvent.SearchButtonTappedEvent)
                    })
                Spacer(modifier = Modifier.width(4.dp))
                OutlinedButton(
                    modifier = Modifier.width(80.dp),
                    onClick = {
                        viewModel.fireEvent(SearchEvent.SearchButtonTappedEvent)
                    }) {
                    Text(
                        modifier = Modifier
                            .wrapContentSize(),
                        text = "검색",
                        maxLines = 1
                    )
                }
            }
            LazyColumn(modifier = Modifier
                .fillMaxWidth(), state = state.lazyListState, content = {
                items(state.searchResult.size) {
                    if (it > state.searchResult.size - 2) {
                        viewModel.fireEvent(SearchEvent.ScrollReachedNearBottomEvent)
                    }
                    val book = state.searchResult[it]
                    BookListTile(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .clickable {
                                viewModel.fireEvent(
                                    SearchEvent.BookListTileTappedEvent(book)
                                )
                            }, book = book
                    )
                }
            })
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    ThothTheme {
        SearchScreen(
            navController = rememberNavController(),
            viewModel = SearchViewModel(
                FakeNaverBookRepositoryImpl(
                    NaverBookSearchResultMapper()
                )
            )
        )
    }
}