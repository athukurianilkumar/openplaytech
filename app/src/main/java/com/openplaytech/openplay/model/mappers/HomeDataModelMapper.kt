package com.openplaytech.openplay.model.mappers

import com.openplaytech.openplay.common.Definitions
import com.openplaytech.openplay.database.MovieDbDatabase
import com.openplaytech.openplay.database.dao.MovieDbTable
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.model.parsers.search.SearchResponse
import javax.inject.Inject

interface HomeDataModelMapper {

    fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel>
    suspend fun toHomeDataModelFromResponse(searchResponse: SearchResponse): List<HomeDataModel>
}

class HomeDataModelMapperImpl @Inject constructor(private val movieDbDatabase: MovieDbDatabase) :
    HomeDataModelMapper {

    override fun toHomeDataModelFromTable(databaseList: List<MovieDbTable>): List<HomeDataModel> {
        return databaseList.map { databaseItem ->
            HomeDataModel(
                id = databaseItem.id,
                title = databaseItem.title,
                mediaType = databaseItem.mediaType,
                summary = databaseItem.summary,
                thumbnail = databaseItem.thumbnail,
                releaseDate = databaseItem.releaseDate,
                ratings = databaseItem.ratings,
                dateAdded = databaseItem.dateAdded,
                genresName = databaseItem.genresName,
            )
        }
    }

    override suspend fun toHomeDataModelFromResponse(searchResponse: SearchResponse): List<HomeDataModel> {
        return (searchResponse.searchResultsList?.map { searchItem ->
            HomeDataModel(
                id = searchItem.id,
                title = searchItem.title ?: searchItem.name ?: searchItem.originalName ?: "-",
                mediaType = searchItem.mediaType ?: "-",
                summary = searchItem.overview ?: "-",
                thumbnail = "${Definitions.IMAGE_URL_W300}${searchItem.posterPath}",
                releaseDate = searchItem.releaseDate ?: searchItem.firstAirDate ?: "-",
                ratings = (searchItem.voteAverage ?: 0).toString(),
            )

        } ?: arrayListOf())
    }
}
