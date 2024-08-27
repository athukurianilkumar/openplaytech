package com.openplaytech.openplay.mvvm.viewModel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.interactor.details.DetailsInteractorImpl
import com.openplaytech.openplay.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val detailsInteractor: DetailsInteractorImpl) : BaseViewModel() {

    var homeDataModel: HomeDataModel? = null
    private lateinit var homeDataModelLiveData: MutableLiveData<HomeDataModel>
    var hasUserChangeFavourite: Boolean? = false

    fun getDetailsList(): LiveData<HomeDataModel> {
        if (!::homeDataModelLiveData.isInitialized) {
            homeDataModelLiveData = MutableLiveData()
            fetchDetails()
        }
        return homeDataModelLiveData
    }

    private fun fetchDetails() {
        if (homeDataModel == null ||
                homeDataModel?.id == null ||
                homeDataModel?.mediaType == null) {
            return
        }

        uiScope.launch {
            loadingLiveData.value = true
            val response = detailsInteractor.onRetrieveDetails(homeDataModel!!)
            response.data?.let {
                homeDataModelLiveData.value = it
            } ?: response.throwable?.let {
                onErrorThrowable(it, true)
            } ?: run {
                genericErrorLiveData.value = true
            }
            loadingLiveData.value = false
        }
    }

    fun setUIModel(homeDataModel: HomeDataModel?) {
        this.homeDataModel = homeDataModel
    }
}