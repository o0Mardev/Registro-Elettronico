package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.mardev.registroelettronico.feature_main.domain.model.Grade

@Composable
fun GradeBySubjectScreen(
    modifier: Modifier = Modifier,
    groupedGrades: Map<String, List<Grade>>
) {
    var selectedSubjects by remember { mutableStateOf(setOf<String>()) }

    val sortedGroupedGrades = groupedGrades.toList().sortedBy { (header, _) -> header }


    LazyColumn(modifier = modifier.then(
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .windowInsetsPadding(WindowInsets.displayCutout)
        } else modifier)) {
        items(
            items = sortedGroupedGrades,
            key = { (header, _) ->
                header
            }
        ) { (header, items) ->
            val isExpanded = selectedSubjects.contains(header)
            val rotation by animateFloatAsState(if (isExpanded) 180f else 0f)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .animateContentSize(),
                onClick = {
                    selectedSubjects = if (isExpanded) {
                        selectedSubjects - header
                    } else {
                        selectedSubjects + header
                    }
                }
            ) {
                Column {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = header,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            val filteredItems =
                                items.filter { it.voteValue != 0f && it.weight != 0f }
                            val sumOfValues =
                                filteredItems.sumOf { (it.voteValue * it.weight).toDouble() }
                            val sumOfWeights = filteredItems.sumOf { it.weight.toDouble() }
                            val avg = sumOfValues / sumOfWeights
                            Text(
                                text = "Media: %.2f".format(avg),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotation)
                        )
                    }

                    if (isExpanded) {
                        PerformanceChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(horizontal = 16.dp),
                            list = items.filter { it.voteValue != 0f && it.weight != 0f }
                                .map { it.voteValue }
                                .asReversed()
                        )

                        HorizontalDivider(
                            thickness = DividerDefaults.Thickness.times(1.5f),
                            color = MaterialTheme.colorScheme.outline
                        )

                        items.fastForEach { grade ->
                            GradeItem(
                                showDivider = true,
                                grade = grade,
                                showSubject = false,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 2.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
            )
        }
    }
}