package com.mardev.registroelettronico.feature_home.presentation.components.communication_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_home.presentation.HomeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationScreen(
    state: CommunicationScreenState,
) {

    Scaffold { paddingValues ->
        if (state.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(state.communications.size) { i ->
                val communication = state.communications[i]
                if (i > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                CommunicationItem(communication = communication)

                if (i < state.communications.size - 1) {
                    Divider()
                }
            }

        }
    }
}