package com.mardev.registroelettronico.feature_authentication.data.repository


import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_authentication.data.remote.AuthenticationApi
import com.mardev.registroelettronico.feature_authentication.domain.model.LoginInfo
import com.mardev.registroelettronico.feature_authentication.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: AuthenticationApi,
) : LoginRepository {

    override fun login(
        taxCode: String,
        username: String,
        password: String
    ): Flow<Resource<LoginInfo>> = flow {
        emit(Resource.Loading())

        try {
            val remoteLoginInfo = api.login(
                taxCode, username, password
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