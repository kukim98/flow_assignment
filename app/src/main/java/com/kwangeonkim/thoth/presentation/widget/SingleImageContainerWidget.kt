package com.kwangeonkim.thoth.presentation.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import com.kwangeonkim.thoth.R
import com.kwangeonkim.thoth.presentation.theme.ThothTheme

@Composable
fun SingleImageContainerWidget(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    contentDescription: String? = null,
    description: String?
) {

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val dim = min(maxWidth, maxHeight).times(0.8f)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(dim),
                painter = painterResource(id = id),
                contentDescription = contentDescription
            )
            description?.let {
                Text(text = it)
            }
        }
    }
}

@Preview
@Composable
fun SingleImageContainerWidgetPreview() {
    ThothTheme {
        SingleImageContainerWidget(id = R.drawable.looney_15, description = "Description")
    }
}