package com.mardev.registroelettronico.core.data.repository

import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.data.remote.AppUpdateApi
import com.mardev.registroelettronico.core.domain.models.AppUpdateInfo
import com.mardev.registroelettronico.core.domain.repository.AppUpdateRepository
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AppUpdateRepositoryImpl @Inject constructor(
    private val api: AppUpdateApi
) : AppUpdateRepository {
    override suspend fun getAppUpdateInfo(): Flow<Resource<AppUpdateInfo>> = flow {
        try {
            emit(Resource.Loading())
            val appUpdateInfo = api.getAppUpdateInfo()
            emit(Resource.Success(appUpdateInfo))
        } catch (e: HttpException) {
            emit(
                Resource.Error(UIText.StringResource(R.string.error1))
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(UIText.StringResource(R.string.error2))
            )
        }
    }

    override suspend fun getAppUpdateApk(url: String): Flow<Resource<ResponseBody>> = flow {
        try {
            emit(Resource.Loading())
            val responseBody = api.downloadFile(url)
            emit(Resource.Success(responseBody))
        } catch (e: HttpException) {
            emit(
                Resource.Error(UIText.StringResource(R.string.error1))
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(UIText.StringResource(R.string.error2))
            )
        }
    }

}