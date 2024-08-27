package com.openplaytech.openplay.mvvm.interactor.details

import com.openplaytech.openplay.common.Definitions
import com.openplaytech.openplay.database.MovieDbDatabase
import com.openplaytech.openplay.model.common.DataResult
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.model.parsers.movie.MovieResponse
import com.openplaytech.openplay.model.parsers.tv.TvShowResponse
import com.openplaytech.openplay.mvvm.interactor.base.BaseInteractor
import com.openplaytech.openplay.network.client.MovieClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetailsInteractorImpl @Inject constructor(private var movieClient: MovieClient, private val movieDbDatabase: MovieDbDatabase) : BaseInteractor(), DetailsInteractor {

    override suspend fun onRetrieveFlowDetails(homeDataModel: HomeDataModel): Flow<DataResult<HomeDataModel>> {
        return flow {
            emit(onRetrieveDetails(homeDataModel))
        }
    }

    override suspend fun onRetrieveDetails(homeDataModel: HomeDataModel): DataResult<HomeDataModel> {
        return try {
            val response = when (homeDataModel.mediaType) {
                Definitions.IS_MOVIE -> movieToHomeDataModel(homeDataModel, movieClient.getMovieDetailsAsync(homeDataModel.id
                        ?: 0))
                else ->
                    tvShowToHomeDataModel(homeDataModel, movieClient.getTvShowDetailsAsync(homeDataModel.id
                        ?: 0))
            }
            DataResult(response)
        } catch (t: Throwable) {
            DataResult(throwable = t)
        }
    }

    private fun movieToHomeDataModel(homeDataModel: HomeDataModel, movieResponse: MovieResponse): HomeDataModel {
        homeDataModel.thumbnail = "${Definitions.IMAGE_URL_W500}${movieResponse.posterPath}"
        homeDataModel.genresName = movieResponse.genres?.firstOrNull()?.name ?: "-"
        return homeDataModel
    }

    private fun tvShowToHomeDataModel(homeDataModel: HomeDataModel, tvShowResponse: TvShowResponse): HomeDataModel {
        homeDataModel.thumbnail = "${Definitions.IMAGE_URL_W500}${tvShowResponse.posterPath}"
        homeDataModel.genresName = tvShowResponse.genres?.firstOrNull()?.name ?: "-"
        return homeDataModel
    }

}
