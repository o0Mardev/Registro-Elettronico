package com.mardev.registroelettronico.feature_authentication.presentation.search_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mardev.registroelettronico.feature_authentication.presentation.search_screen.SearchViewModel

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: SearchViewModel = hiltViewModel()
    val searchText by viewModel.searchText.collectAsState()
    val schools by viewModel.schools.collectAsState(emptyList())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            supportingText = {
                Text(text = "Inserisci il nome della scuola, il comune o il CAP")

            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            placeholder = { Text(text = "Cerca la tua scuola") },
            leadingIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        viewModel.onCancelClick()
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            singleLine = true,
            value = searchText,
            onValueChange = viewModel::onSearchTextChange
        )
        LazyColumn(modifier = Modifier.padding(4.dp)) {
            items(items = schools, key = { school ->
                school.taxCode
            }) { school ->
                SchoolItem(school = school) { taxCode ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("taxCode", taxCode)
                    navController.popBackStack()
                }
                Divider(thickness = DividerDefaults.Thickness.times(2))
            }
        }
    }
}