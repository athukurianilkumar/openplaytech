package com.openplaytech.openplay.mvvm.interactor.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.openplaytech.openplay.common.Definitions
import com.openplaytech.openplay.database.MovieDbDatabase
import com.openplaytech.openplay.model.common.DataResult
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.model.mappers.HomeDataModelMapperImpl
import com.openplaytech.openplay.model.parsers.search.SearchResponse
import com.openplaytech.openplay.mvvm.interactor.base.BaseInteractor
import com.openplaytech.openplay.mvvm.interactor.home.paging.SearchPagingDataSource
import com.openplaytech.openplay.network.client.MovieClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber


class HomeInteractorImpl(
    private var movieClient: MovieClient,
    private val movieDbDatabase: MovieDbDatabase,
    private val mapper: HomeDataModelMapperImpl
) : BaseInteractor(), HomeInteractor {

    override suspend fun onRetrieveSearchResult(
        queryText: String,
        page: Int
    ): Flow<DataResult<List<HomeDataModel>>> {
        return try {
            val response = movieClient.getSearchAsync(queryText, page)
            flow {
                emit(DataResult(toHomeDataModel(response)))
            }
        } catch (t: Throwable) {
            Timber.d(t)
            flow { DataResult(null, throwable = t) }
        }
    }


    override suspend fun flowPaging(queryText: String): Flow<PagingData<HomeDataModel>> {


        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            PagingConfig(
                pageSize = 20
            )
        ) {
            SearchPagingDataSource(queryText, movieClient, mapper)
        }.flow
    }

    private fun toHomeDataModel(searchResponse: SearchResponse): List<HomeDataModel> {
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
