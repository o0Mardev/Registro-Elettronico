package com.mardev.registroelettronico.feature_main.common.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateItem(
    date: Date,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    val sdf = SimpleDateFormat("EEEE dd MMMM", Locale("it", "IT"))
    Text(
        text = sdf.format(date),
        style = style,
        modifier = modifier,
    )
}