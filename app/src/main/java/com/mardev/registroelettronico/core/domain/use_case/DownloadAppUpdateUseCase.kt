package com.mardev.registroelettronico.core.domain.use_case

import com.mardev.registroelettronico.core.domain.repository.AppUpdateRepository
import com.mardev.registroelettronico.core.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class DownloadAppUpdateUseCase @Inject constructor(
    private val repository: AppUpdateRepository
) {
    suspend operator fun invoke(url: String): Flow<Resource<ResponseBody>> {
            return repository.getAppUpdateApk(url)
    }
}