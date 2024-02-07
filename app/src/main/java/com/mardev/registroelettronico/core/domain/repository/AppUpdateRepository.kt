package com.mardev.registroelettronico.core.domain.repository

import com.mardev.registroelettronico.core.domain.models.AppUpdateInfo
import com.mardev.registroelettronico.core.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface AppUpdateRepository {

    suspend fun getAppUpdateInfo(): Flow<Resource<AppUpdateInfo>>

    suspend fun getAppUpdateApk(url: String): Flow<Resource<ResponseBody>>

}