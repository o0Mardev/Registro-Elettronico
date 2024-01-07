package com.mardev.registroelettronico.feature_authentication.data.repository


import com.google.gson.Gson
import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.data.remote.AxiosApi
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_authentication.domain.model.LoginInfo
import com.mardev.registroelettronico.feature_authentication.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: AxiosApi,
) : LoginRepository {
    private val gson = Gson()

    override fun login(
        request: JsonRequest
    ): Flow<Resource<LoginInfo>> = flow {
        emit(Resource.Loading())

        try {
            val remoteLoginInfo = api.login(
                gson.toJson(request)
            ).toLoginInfo()

            emit(Resource.Success(remoteLoginInfo))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.error1),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.error2),
                    data = null
                )
            )
        }
    }
}