package com.openplaytech.openplay.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import com.openplaytech.openplay.model.data.HomeDataModel
import com.openplaytech.openplay.mvvm.interactor.home.HomeInteractorImpl
import com.openplaytech.openplay.mvvm.viewModel.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractorImpl
) : BaseViewModel() {

    private var queryText = ""

    private val currentQuery = MutableLiveData("")

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState


    fun searchMovies() {
        currentQuery.value = queryText
    }

    fun setQueryText(queryText: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        val resul = homeInteractor.flowPaging(queryText).cachedIn(viewModelScope)
        delay(200)

        _uiState.update { it.copy(isLoading = false, data = resul) }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val data: Flow<PagingData<HomeDataModel>>? = null
)