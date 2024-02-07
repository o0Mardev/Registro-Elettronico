package com.mardev.registroelettronico.core.domain.use_case

import com.mardev.registroelettronico.core.domain.repository.AppUpdateRepository
import com.mardev.registroelettronico.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetUpdateUrlUseCase @Inject constructor(
    private val repository: AppUpdateRepository
) {
    suspend operator fun invoke(): Flow<Resource<String>> {
        return repository.getAppUpdateInfo()
            .transform { appUpdateInfoResource ->
                when (appUpdateInfoResource) {
                    is Resource.Success -> {
                        appUpdateInfoResource.data?.let {
                            emit(Resource.Success(it.downloadUrl))
                        }
                    }

                    is Resource.Error -> {
                        appUpdateInfoResource.uiText?.let {
                            emit(Resource.Error(it))
                        }
                    }

                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }
    }
}