package com.bluecoder.cloudcastle.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repo: FilesRepo
) : ViewModel()  {


    val photosState : StateFlow<MainUiState> =
        repo.getFiles("image,video")
            .map {
                if(it.isSuccess)
                    MainUiState.Success(it.getOrNull()!!)
                else if(it.isFailure)
                    MainUiState.Error
                else
                    MainUiState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = MainUiState.Loading,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    val documentsUiState : StateFlow<MainUiState> =
        repo.getFiles("application")
            .map {
                if(it.isSuccess)
                    MainUiState.Success(it.getOrNull()!!)
                else if(it.isFailure)
                    MainUiState.Error
                else
                    MainUiState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = MainUiState.Loading,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    val soundsUiState : StateFlow<MainUiState> =
        repo.getFiles("audio")
            .map {
                if(it.isSuccess)
                    MainUiState.Success(it.getOrNull()!!)
                else if(it.isFailure)
                    MainUiState.Error
                else
                    MainUiState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = MainUiState.Loading,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    val imagesCount : StateFlow<Int> =
        repo.getFilesCountByType("image")
            .map {
                if(it.isSuccess)
                    it.getOrNull()!!.count
                else
                    0
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = 0,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    val videosCount : StateFlow<Int> =
        repo.getFilesCountByType("video")
            .map {
                if(it.isSuccess)
                    it.getOrNull()!!.count
                else
                    0
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = 0,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    val documentsCount : StateFlow<Int> =
        repo.getFilesCountByType("application")
            .map {
                if(it.isSuccess)
                    it.getOrNull()!!.count
                else
                    0
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = 0,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )

    val soundsCount : StateFlow<Int> =
        repo.getFilesCountByType("audio")
            .map {
                if(it.isSuccess)
                    it.getOrNull()!!.count
                else
                    0
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = 0,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)
            )
}