package com.mardev.registroelettronico.feature_home.presentation.components.grade_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mardev.registroelettronico.feature_home.domain.model.Grade
import com.mardev.registroelettronico.feature_home.presentation.components.common.DateItem
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GradeItem(
    grade: Grade,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = grade.vote, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = grade.subject)
        Text(text = grade.teacher)
        Text(text = grade.description)
        DateItem(date = grade.date)
        Spacer(modifier = Modifier.height(8.dp))
    }
}