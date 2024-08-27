package com.openplaytech.openplay.model.parsers.tv

import com.google.gson.annotations.SerializedName
import com.openplaytech.openplay.model.parsers.genre.GenresItem

data class TvShowResponse(
    @SerializedName("type") val type: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("popularity") val popularity: Double? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int? = null,
    @SerializedName("vote_count") val voteCount: Int? = null,
    @SerializedName("genres") val genres: List<GenresItem>? = null,
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("original_name") val originalName: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("homepage") val homepage: String? = null)
