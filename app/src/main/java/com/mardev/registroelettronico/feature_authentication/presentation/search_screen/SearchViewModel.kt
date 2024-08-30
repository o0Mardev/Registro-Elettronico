package com.mardev.registroelettronico.feature_authentication.presentation.search_screen

import androidx.lifecycle.ViewModel
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.model.School
import com.mardev.registroelettronico.feature_authentication.domain.use_case.SearchSchoolUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchSchoolUseCase: SearchSchoolUseCase
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _schools = MutableStateFlow(emptyList<School>())
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val schools: Flow<List<School>> = searchText
        .debounce(300L)
        .combine(_schools) { textQuery, schools ->
            if (textQuery.isEmpty()) {
                flowOf(schools)
            } else {
                searchSchoolUseCase(textQuery).map { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data ?: emptyList()
                        }
                        else -> {
                            emptyList()
                        }
                    }
                }
            }
        }
        .flattenMerge()


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCancelClick() {
        _searchText.value = ""
        _schools.value = emptyList()
    }

}