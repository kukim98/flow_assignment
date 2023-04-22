package com.kwangeonkim.thoth.presentation.screen.search_screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kwangeonkim.thoth.R
import com.kwangeonkim.thoth.domain.model.NaverBook
import com.kwangeonkim.thoth.presentation.theme.ThothTheme

@Composable
fun BookListTile(modifier: Modifier = Modifier, book: NaverBook) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier.padding(4.dp).weight(1f),
            model = book.image,
            contentDescription = "Book Image",
            placeholder = debugPlaceholder(R.drawable.book_placeholder)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.padding(4.dp).weight(4f)) {
            Text(text = "제목: ${book.title}", maxLines = 3, overflow = TextOverflow.Ellipsis)
            Text(text = "저자: ${book.author}")
            Text(text = "출판사: ${book.publisher}")
            Text(text = "가격: ${book.price}")
        }
    }
}

@Composable
fun debugPlaceholder(@DrawableRes debugPreview: Int): Painter? {
    return if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        null
    }
}

@Preview
@Composable
fun BookListTilePreview() {
    ThothTheme {
        BookListTile(book = NaverBook.sample())
    }
}