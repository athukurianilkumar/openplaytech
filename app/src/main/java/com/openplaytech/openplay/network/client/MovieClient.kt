package com.openplaytech.openplay.network.client

import com.openplaytech.openplay.common.Definitions
import com.openplaytech.openplay.model.parsers.movie.MovieResponse
import com.openplaytech.openplay.model.parsers.search.SearchResponse
import com.openplaytech.openplay.model.parsers.tv.TvShowResponse
import com.openplaytech.openplay.network.api.MovieApi
import javax.inject.Inject

class MovieClient @Inject constructor(private var movieApi: MovieApi) {

    suspend fun getSearchAsync(queryText: String, page: Int): SearchResponse {
        return movieApi.getSearchAsync(Definitions.API_KEY, queryText, page)
    }

    suspend fun getMovieDetailsAsync(movieId: Int): MovieResponse {
        return movieApi.getMovieDetailsAsync(movieId, Definitions.API_KEY, Definitions.VIDEOS)
    }

    suspend fun getTvShowDetailsAsync(tvId: Int): TvShowResponse {
        return movieApi.getTvShowDetailsAsync(tvId, Definitions.API_KEY, Definitions.VIDEOS)
    }

}