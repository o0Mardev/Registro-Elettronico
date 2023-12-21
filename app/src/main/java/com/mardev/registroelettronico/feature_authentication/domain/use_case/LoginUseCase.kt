package com.mardev.registroelettronico.feature_authentication.domain.use_case

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.model.LoginInfo
import com.mardev.registroelettronico.feature_authentication.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(
    private val repository: LoginRepository
) {

    operator fun invoke(
        taxCode: String,
        username: String,
        password: String
    ): Flow<Resource<LoginInfo>> {
        //Implement here validation logic
        return repository.login(taxCode, username, password)
    }

}