package com.mardev.registroelettronico.feature_main.presentation.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateItem(
    date: LocalDate,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    val sdf = DateTimeFormatter.ofPattern("EEEE dd MMMM")
    Text(
        text = sdf.format(date),
        style = style,
        modifier = modifier,
    )
}