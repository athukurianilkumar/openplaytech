package com.openplaytech.openplay.mvvm.viewModel.details.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.interactor.details.DetailsInteractorImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsComposeViewModel @Inject constructor(private val detailsInteractor: DetailsInteractorImpl) : ViewModel() {

    var homeDataModel: HomeDataModel? = null

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    private fun fetchDetails() {
        if (homeDataModel == null ||
                homeDataModel?.id == null ||
                homeDataModel?.mediaType == null) {
            return
        }

        viewModelScope.launch {
            detailsInteractor.onRetrieveFlowDetails(homeDataModel!!).collect { response ->
                response.data?.let {
                    _uiState.value = DetailsUiState.Success(it)
                } ?: run {
                    _uiState.value = DetailsUiState.Error
                }
            }
        }
    }

    fun setUIModel(homeDataModel: HomeDataModel?) {
        this.homeDataModel = homeDataModel
        fetchDetails()
    }
}

sealed class DetailsUiState {

    data object Loading : DetailsUiState()
    data class Success(val homeDataModel: HomeDataModel) : DetailsUiState()
    data object Error : DetailsUiState()
}
