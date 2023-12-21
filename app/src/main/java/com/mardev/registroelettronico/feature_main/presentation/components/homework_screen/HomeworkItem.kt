package com.mardev.registroelettronico.feature_home.presentation.components.homework_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mardev.registroelettronico.feature_home.domain.model.Homework
import com.mardev.registroelettronico.feature_home.presentation.components.common.DateItem
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeworkItem(
    homework: Homework,
    modifier: Modifier = Modifier,
    onCheckedChange: ((id: Int, state: Boolean) -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = modifier.weight(1f)) {
            Text(text = homework.subject, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = homework.description)
            Spacer(modifier = Modifier.height(4.dp))
            DateItem(date = homework.dueDate)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (onCheckedChange!=null){
            Checkbox(checked = homework.completed, onCheckedChange = { state ->
                onCheckedChange(homework.id, state)
            })
        }
    }
}