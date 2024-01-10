package com.mardev.registroelettronico.feature_authentication.domain.use_case

import com.google.gson.Gson
import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.data.remote.AxiosApi
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_authentication.domain.model.School
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchSchoolUseCase(
    private val api: AxiosApi
) {
    private val gson = Gson()

    suspend operator fun invoke(textQuery: String): Flow<Resource<List<School>>> = flow {
        if (textQuery.isNotBlank()) {
            val request = JsonRequest(
                sSearch = textQuery
            )

            try {
                val remoteSchools = api.searchForSchools(gson.toJson(request)).response?.map { it.toSchool() }
                if (remoteSchools!=null) {
                    emit(Resource.Success(remoteSchools))
                }
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        uiText = UIText.StringResource(R.string.error1), data = null
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
}