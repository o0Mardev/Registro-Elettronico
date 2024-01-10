package com.mardev.registroelettronico.feature_authentication.presentation.search_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mardev.registroelettronico.feature_authentication.domain.model.School

@Composable
fun SchoolItem(school: School, onClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable {
        onClick(school.taxCode)
    }) {
            Text(text = "${school.type} ${school.name}")
            Text(text = "${school.region} ${school.city} ${school.province}")
    }
}