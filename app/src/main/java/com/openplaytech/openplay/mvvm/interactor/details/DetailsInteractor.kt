package com.openplaytech.openplay.mvvm.interactor.details

import com.openplaytech.openplay.model.common.DataResult
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.interactor.base.MVVMInteractor
import kotlinx.coroutines.flow.Flow

interface DetailsInteractor : MVVMInteractor {

    suspend fun onRetrieveFlowDetails(homeDataModel: HomeDataModel): Flow<DataResult<HomeDataModel>>
    suspend fun onRetrieveDetails(homeDataModel: HomeDataModel): DataResult<HomeDataModel>
}
