package com.mardev.registroelettronico.feature_authentication.domain.repository

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.model.LoginInfo
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun login(
        taxCode: String,
        username: String,
        password: String
    ): Flow<Resource<LoginInfo>>

}