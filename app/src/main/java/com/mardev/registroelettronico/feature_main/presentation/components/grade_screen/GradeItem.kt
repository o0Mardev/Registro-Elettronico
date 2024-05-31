package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Grade
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem
import java.time.LocalDate

@Composable
fun GradeItem(
    grade: Grade,
    modifier: Modifier = Modifier,
    showSubject: Boolean = true,
    showDate: Boolean = true,
    showOverline: Boolean = false,
    showIcon: Boolean = false,
    showDivider: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        if (showIcon) {
            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.DarkGray.copy(alpha = 0.75f),
                            radius = this.size.maxDimension / 1.3f
                        )
                    },
                imageVector = Icons.TwoTone.Grade,
                tint = Color.White,
                contentDescription = null
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = modifier.weight(1f)) {
                if (showOverline) {
                    Text(text = "Voto", style = MaterialTheme.typography.labelMedium)
                }
                if (showSubject){
                    Text(text = grade.subject, style = MaterialTheme.typography.bodyLarge)
                }
                Text(text = grade.teacher, style = MaterialTheme.typography.bodyMedium)

                if (grade.description.isBlank()) {
                    Text(text = "Nesssuna descrizione", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text(text = grade.description, style = MaterialTheme.typography.bodyMedium)
                }
                if (showDate) {
                    DateItem(date = grade.date)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (grade.weight == 0f || grade.voteValue == 0f) {
                                Color(66, 139, 202)
                            } else if (grade.voteValue >= 6f) {
                                Color(53, 170, 71)
                            } else if (grade.voteValue < 6f) {
                                Color(216, 74, 56)
                            } else Color.Unspecified
                        ),
                ) {
                    Text(
                        text = grade.vote,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Text(text = "Peso: %.0f ".format(grade.weight), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    if (showDivider) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGradeItem(modifier: Modifier = Modifier) {
    GradeItem(
        showDate = false,
        grade = Grade(
            id = 0,
            subject = "ITALIANO",
            vote= "10",
            description = "Interrogazione orale",
            date = LocalDate.now(),
            idTimeFraction = 0,
            teacher = "PINCO PALLO",
            weight = 100f,
            voteValue = 10f,
    ))
}

@Preview(showBackground = true)
@Composable
fun PreviewGradeItem2(modifier: Modifier = Modifier) {
    GradeItem(
        showOverline = true,
        showIcon = true,
        grade = Grade(
            id = 0,
            subject = "ITALIANO",
            vote= "10",
            description = "Interrogazione orale",
            date = LocalDate.now(),
            idTimeFraction = 0,
            teacher = "PINCO PALLO",
            weight = 100f,
            voteValue = 10f,
        ))
}