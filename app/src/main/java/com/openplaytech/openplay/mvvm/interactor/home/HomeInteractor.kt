package com.openplaytech.openplay.mvvm.interactor.home

import androidx.paging.PagingData
import com.openplaytech.openplay.model.common.DataResult
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.interactor.base.MVVMInteractor
import kotlinx.coroutines.flow.Flow

interface HomeInteractor : MVVMInteractor {

    suspend fun onRetrieveSearchResult(
        queryText: String, page: Int
    ): Flow<DataResult<List<HomeDataModel>>>

    suspend fun flowPaging(queryText: String): Flow<PagingData<HomeDataModel>>
}
