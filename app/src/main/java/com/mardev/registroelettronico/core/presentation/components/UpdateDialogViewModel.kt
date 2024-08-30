package com.mardev.registroelettronico.core.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.domain.use_case.DownloadAppUpdateUseCase
import com.mardev.registroelettronico.core.domain.use_case.GetUpdateUrlUseCase
import com.mardev.registroelettronico.core.domain.use_case.InstallAppUpdateUseCase
import com.mardev.registroelettronico.core.domain.use_case.IsUpdateAvailableUseCase
import com.mardev.registroelettronico.core.domain.use_case.SaveUpdateUseCase
import com.mardev.registroelettronico.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateDialogViewModel @Inject constructor(
    private val isUpdateAvailableUseCase: IsUpdateAvailableUseCase,
    private val getUpdateUrlUseCase: GetUpdateUrlUseCase,
    private val downloadAppUpdateUseCase: DownloadAppUpdateUseCase,
    private val saveUpdateUseCase: SaveUpdateUseCase,
    private val installAppUpdateUseCase: InstallAppUpdateUseCase
) : ViewModel() {


    private val _state = mutableStateOf(UpdateDialogState())
    val state: State<UpdateDialogState> = _state

    init {
        viewModelScope.launch {
            delay(1000)
            isUpdateAvailable()
        }
    }

    fun changeUpdateDialogVisibility(visible: Boolean) {
        _state.value = _state.value.copy(showUpdateDialog = visible)
    }

    fun changeUpdateDialogButtonsVisibility(visible: Boolean) {
        _state.value = _state.value.copy(showButtons = visible)
    }

    /**
     * Public function used to update the app
     */
    suspend fun updateApp(context: Context) {
        val url = withContext(Dispatchers.IO) {
            getUpdateUrl()
        }
        val file = File(context.filesDir, "update.apk")

        _state.value = _state.value.copy(progress = 0f)
        _state.value = _state.value.copy(showProgress = true)

        url.onEach { resultingUrl ->
            downloadAppUpdate(resultingUrl).onEach { resultingBody ->
                saveAppUpdate(resultingBody, file).onEach { progress ->
                    when (progress) {
                        1f -> {
                            //Download finished
                            _state.value = _state.value.copy(showProgress = false)
                            changeUpdateDialogVisibility(false)
                            installAppUpdate(context, file)
                        }

                        else -> {
                            _state.value = _state.value.copy(progress = progress)
                        }
                    }
                }.launchIn(viewModelScope)
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    private fun installAppUpdate(context: Context, file: File) {
        installAppUpdateUseCase(file, context)
    }

    private suspend fun saveAppUpdate(body: ResponseBody, file: File): Flow<Float> {
        return saveUpdateUseCase(body, file).transform { result ->
            when (result) {
                is Resource.Loading -> {
                    result.data?.let { emit(it) }
                }

                is Resource.Success -> {
                    Timber.i("Succeed in saving app update")
                }


                is Resource.Error -> {
                    changeUpdateDialogVisibility(false)
                    Timber.e("Error while saving app update")
                }

            }
        }
    }


    private suspend fun downloadAppUpdate(url: String): Flow<ResponseBody> {
        return downloadAppUpdateUseCase(url).transform { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { emit(it) }
                    Timber.i("Succeed in downloading app update")
                }

                is Resource.Error -> {
                    Timber.e("Error while downloading app update")
                }

                is Resource.Loading -> {
                    Timber.i("Loading app update")
                }
            }
        }
    }

    private suspend fun getUpdateUrl(): Flow<String> {
        return getUpdateUrlUseCase().transform { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { emit(it) }
                    Timber.i("Succeed in getting updateUrl: ${result.data}")
                }

                is Resource.Error -> {
                    Timber.e("Error while getting updateUrl")
                }

                is Resource.Loading -> {
                    Timber.i("Loading updateUrl")
                }
            }
        }
    }

    private suspend fun isUpdateAvailable() {

        isUpdateAvailableUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Timber.i("Succeed to check update with result: " + result.data)
                    result.data?.let {
                        _state.value =
                            _state.value.copy(showUpdateDialog = it)
                    }
                }

                is Resource.Error -> {
                    Timber.e("Error while checking update")
                }

                is Resource.Loading -> {
                    Timber.i("Loading to check update")
                }

            }
        }.launchIn(viewModelScope)
    }

}
