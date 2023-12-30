package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.Grade

@Composable
fun GradeBySubjectScreen(
    modifier: Modifier,
    groupedGrades: Map<String, List<Grade>>
) {
    var selectedSubjects by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(modifier = modifier) {
        groupedGrades.forEach { (header, items) ->
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            selectedSubjects = if (selectedSubjects.contains(header)) {
                                selectedSubjects - header
                            } else {
                                selectedSubjects + header
                            }
                        }
                        .background(MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small),
                ) {
                    Text(text = header)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
                if (selectedSubjects.contains(header)) {
                    items.forEach { grade ->
                        Spacer(modifier = Modifier.height(4.dp))
                        GradeItem(grade = grade, showSubject = false)
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(thickness = DividerDefaults.Thickness.times(2))
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
