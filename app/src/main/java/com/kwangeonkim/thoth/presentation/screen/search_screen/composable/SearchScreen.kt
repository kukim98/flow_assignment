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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kwangeonkim.thoth.R
import com.kwangeonkim.thoth.data.repository.fake_repository.FakeNaverBookRepositoryImpl
import com.kwangeonkim.thoth.domain.mapper.NaverBookSearchResultMapper
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.presentation.Resource
import com.kwangeonkim.thoth.presentation.screen.Screen
import com.kwangeonkim.thoth.presentation.theme.ThothTheme
import com.kwangeonkim.thoth.presentation.widget.LoadingWidget
import com.kwangeonkim.thoth.presentation.widget.SingleImageContainerWidget
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    savedStateHandle: SavedStateHandle
) {

    val state = viewModel.state.value
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val books = viewModel.books.collectAsState().value

    LaunchedEffect(key1 = true) {
        savedStateHandle.get<String>("text")?.let {
            viewModel.fireEvent(SearchEvent.SearchTextFormFieldValueChangedEvent(it))
            viewModel.fireEvent(SearchEvent.SearchButtonTappedEvent)
        }

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

    LaunchedEffect(state.lazyListState) {
        state.lazyListState.scrollToItem(
            state.lazyListState.firstVisibleItemIndex,
            state.lazyListState.firstVisibleItemScrollOffset
        )
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
                .fillMaxWidth()
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val width = maxWidth
                val buttonSize = 100.dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.width(width - buttonSize),
                        value = state.text,
                        onValueChange = {
                            viewModel.fireEvent(
                                SearchEvent.SearchTextFormFieldValueChangedEvent(it)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions {
                            viewModel.fireEvent(SearchEvent.SearchButtonTappedEvent)
                        })
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedButton(
                        modifier = Modifier.wrapContentWidth(),
                        enabled = state.text.isNotBlank(),
                        onClick = {
                            viewModel.fireEvent(SearchEvent.SearchButtonTappedEvent)
                        }) {
                        Text(
                            modifier = Modifier.width(buttonSize),
                            textAlign = TextAlign.Center,
                            text = "검색",
                            maxLines = 1
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth(),
                    state = state.lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        items(books.size) {
                            val book = books[it]
                            BookListTile(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clickable {
                                        viewModel.fireEvent(
                                            SearchEvent.BookListTileTappedEvent(book)
                                        )
                                    }, book = book
                            )
                            if (it == books.size - 1 && !state.noMoreQuery) {
                                CircularProgressIndicator()
                            }
                            if (it == books.size - 1 && !state.noMoreQuery && state.searchStatus != Resource.Loading) {
                                viewModel.fireEvent(SearchEvent.ScrollReachedNearBottomEvent)
                            }
                        }
                    })
                when (state.searchStatus) {
                    is Resource.Loading -> {
                        if (state.displaySearchStatusInFull)
                            LoadingWidget(modifier = Modifier.fillMaxSize())
                    }
                    is Resource.Success -> Unit
                    is Resource.Failure -> {
                        if (state.displaySearchStatusInFull)
                            SingleImageContainerWidget(
                                modifier = Modifier
                                    .padding(it)
                                    .padding(16.dp)
                                    .fillMaxSize(),
                                id = R.drawable.looney_15,
                                description = (state.searchStatus as Resource.Failure).message
                            )
                        else
                            LoadingWidget(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            )
                    }
                }
            }
        }
    }
}

class SearchScreenPreviewViewModelProvider : PreviewParameterProvider<SearchViewModel> {
    override val values = sequenceOf(
        SearchViewModel(
            FakeNaverBookRepositoryImpl(
                NaverBookSearchResultMapper()
            )
        ),
        SearchViewModel(
            FakeNaverBookRepositoryImpl(
                NaverBookSearchResultMapper()
            ).apply {
                text = "Chicken"
                naverBooks.value = listOf(
                    NaverBook.sample(),
                    NaverBook.sample(),
                    NaverBook.sample(),
                    NaverBook.sample(),
                    NaverBook.sample()
                )
            }
        ),
        SearchViewModel(
            FakeNaverBookRepositoryImpl(
                NaverBookSearchResultMapper()
            ).apply {
                text = "Example"
            }
        )
    )
}

@Preview
@Composable
fun SearchScreenPreview(
    @PreviewParameter(SearchScreenPreviewViewModelProvider::class) viewModel: SearchViewModel
) {
    ThothTheme {
        SearchScreen(
            navController = rememberNavController(),
            viewModel = viewModel,
            savedStateHandle = SavedStateHandle()
        )
    }
}
