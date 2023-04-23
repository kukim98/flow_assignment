package com.kwangeonkim.thoth.presentation.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.kwangeonkim.thoth.R
import com.kwangeonkim.thoth.presentation.theme.ThothTheme

@Composable
fun LoadingWidget(modifier: Modifier = Modifier) {

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun LoadingWidgetPreview() {
    ThothTheme {
        LoadingWidget(modifier = Modifier.fillMaxWidth().height(80.dp))
    }
}