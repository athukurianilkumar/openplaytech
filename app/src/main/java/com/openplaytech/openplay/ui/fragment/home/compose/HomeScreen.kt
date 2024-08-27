package com.openplaytech.openplay.ui.fragment.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.ui.compose.MovieDBTheme
import com.openplaytech.openplay.ui.compose.PrimaryDark
import com.openplaytech.openplay.ui.fragment.favourite.card.FavouriteRow
import com.openplaytech.openplay.ui.fragment.home.HomeUiState
import com.openplaytech.openplay.ui.fragment.home.HomeViewModel
import gr.pchasapis.moviedb.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onItemClicked: (HomeDataModel) -> Unit
) {

    val movies by homeViewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = movies,
        textChanged = {
            homeViewModel.setQueryText(it)
            homeViewModel.searchMovies()
        },
        onItemClicked = {
            onItemClicked(it)
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    textChanged: (String) -> Unit = {},
    onItemClicked: (HomeDataModel) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {

        ToolbarCenterAligned {

        }
        var text by remember { mutableStateOf("Example") }

        SearchView(
            text = text,
            placeHolder = "Search it"
        ) {
            text = it

        }
        LaunchedEffect(key1 = text) {
            if (text.isBlank()) return@LaunchedEffect
            delay(1000)
            textChanged(text.trim())
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator(
                color = PrimaryDark,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }

        state.data?.let {
            HomeList(
                messages = it,
                onItemClicked = onItemClicked
            )
        }


    }

}

@Composable
fun HomeList(messages: Flow<PagingData<HomeDataModel>>?, onItemClicked: (HomeDataModel) -> Unit) {

    val lazyPagingItems = messages?.collectAsLazyPagingItems()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
    ) {
        items(lazyPagingItems!!.itemCount) {
            val favourite = lazyPagingItems[it]!!
            FavouriteRow(homeDataModel = favourite) { model ->
                onItemClicked(model)
            }
        }

        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Pagination Loading")

                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "initial Error")

                }
            }
        }
        if (lazyPagingItems.loadState.append is LoadState.Error) {
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Pagination Error")

                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewLoadingHome() {
    MovieDBTheme {

        HomeScreen(state = HomeUiState(isLoading = true))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolbarCenterAligned(onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.home_toolbar_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun SearchView(
    text: String,
    placeHolder: String = "",
    textChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = textChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
        ),
        placeholder = {
            Text(text = placeHolder, color = Color.Black.copy(alpha = 0.2f))
        },
        trailingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
        },
    )
}

@Preview
@Composable
fun PreviewToolbar() {
    MovieDBTheme {
        ToolbarCenterAligned({})
    }
}

@Preview
@Composable
fun PreviewSearch() {
    MovieDBTheme {
        SearchView("search...") {

        }
    }
}