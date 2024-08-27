package com.openplaytech.openplay.ui.fragment.favourite.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.ui.compose.ColorWhite
import com.openplaytech.openplay.ui.fragment.details.ComposeText
import com.openplaytech.openplay.ui.fragment.details.MovieImage
import gr.pchasapis.moviedb.R

@Composable
fun FavouriteRow(
    homeDataModel: HomeDataModel,
    onRowClicked: (HomeDataModel) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clickable {
            onRowClicked(homeDataModel)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        FavouriteContent(homeDataModel = homeDataModel)
    }
}

@Composable
fun FavouriteContent(homeDataModel: HomeDataModel) {
    Row(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {

        MovieImage(
            homeDataModel.thumbnail
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {

            ComposeText(
                text = homeDataModel.title ?: "-",
                maxLines = 2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp)
            )

            ComposeText(
                text = "${stringResource(R.string.home_release_data)}  ${homeDataModel.releaseDate}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

            ComposeText(
                text = "${stringResource(R.string.home_rating)}  ${homeDataModel.ratings}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


