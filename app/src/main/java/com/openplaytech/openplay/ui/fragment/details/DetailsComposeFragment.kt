package com.openplaytech.openplay.ui.fragment.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.viewModel.details.compose.DetailsComposeViewModel
import com.openplaytech.openplay.mvvm.viewModel.details.compose.DetailsUiState
import com.openplaytech.openplay.ui.compose.ColorBlack
import com.openplaytech.openplay.ui.compose.ColorWhite
import com.openplaytech.openplay.ui.compose.MovieDBTheme
import com.openplaytech.openplay.ui.compose.Primary
import com.openplaytech.openplay.ui.compose.PrimaryDark
import gr.pchasapis.moviedb.R

@AndroidEntryPoint
class DetailsComposeFragment : Fragment() {

    private val detailsViewModel: DetailsComposeViewModel by viewModels()

    private val args: DetailsComposeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            detailsViewModel.setUIModel(args.homeDataModel)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MovieDBTheme {

                }
            }
        }
    }
}

@Composable
fun DetailsRoute(
    detailsViewModel: DetailsComposeViewModel = hiltViewModel(), passData: HomeDataModel?
) {
    detailsViewModel.setUIModel(passData)

    val uiState by detailsViewModel.uiState.collectAsState()
    when (uiState) {
        is DetailsUiState.Success -> {
            val model = (uiState as DetailsUiState.Success).homeDataModel
            SuccessCompose(model, detailsViewModel) {
                //  onBackPressed()
            }
        }

        is DetailsUiState.Loading -> {
            LoadingCompose()
        }

        is DetailsUiState.Error -> {

        }

        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingCompose() {
    Surface(
        color = PrimaryDark, modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SuccessCompose(
    homeDataModel: HomeDataModel? = null,
    viewModel: DetailsComposeViewModel? = null,
    onBackIconClicked: () -> Unit
) {
    Surface(
        color = ColorWhite, modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            ToolbarCompose(
                title = homeDataModel?.title ?: "",
            )
            Spacer(modifier = Modifier.size(14.dp))
            ContentCompose(homeDataModel)
        }
    }
}

@Composable
fun ContentCompose(homeDataModel: HomeDataModel?) {

    Column(
        modifier = Modifier
            .padding(10.dp) // Optional padding for overall spacing
            .fillMaxWidth(), // Make column fill the width of the parent
        horizontalAlignment = Alignment.CenterHorizontally // Centers the image horizontally
    ) {
        MovieImage(
            homeDataModel?.thumbnail
        )
    }
    // Using Column to arrange text and image vertically
    Column(
        modifier = Modifier
            .padding(10.dp) // Optional padding for overall spacing
            .fillMaxWidth() // Make column fill the width of the parent
    ) {
        // Summary text
        ComposeText(
            text = homeDataModel?.summary ?: "-",
            maxLines = 6,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Row for genre text
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
        ) {
            ComposeText(
                text = stringResource(R.string.details_genre),
                modifier = Modifier.padding(end = 4.dp)
            )

            ComposeText(
                text = homeDataModel?.genresName ?: "-",
                maxLines = 2,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun test() {
    MovieImage(null)
}

@Composable
fun MovieImage(thumbnail: String?) {
    AsyncImage(
        model = thumbnail,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(120.dp)
    )
}

@Composable
fun ToolbarCompose(
    title: String
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Primary)
    ) {
        Text(
            text = title,
            color = ColorWhite,
            fontSize = 18.sp,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieDBTheme {
        SuccessCompose {}
    }
}

@Composable
fun ComposeText(
    text: String = "-",
    fontSize: TextUnit = 12.sp,
    maxLines: Int = 1,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        color = ColorBlack,
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        fontWeight = fontWeight
    )
}


