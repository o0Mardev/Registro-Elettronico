package com.mardev.registroelettronico.feature_home.presentation.components.communication_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.sp
import com.mardev.registroelettronico.feature_home.domain.model.Communication
import com.mardev.registroelettronico.feature_home.presentation.components.common.DateItem

@Composable
fun CommunicationItem(
    communication: Communication,
    modifier: Modifier = Modifier
) {
    val alphaModifier = if (communication.read) {
        modifier.alpha(ContentAlpha.medium)
    } else modifier

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier.weight(1f)) {
            Text(
                text = communication.title,
                fontSize = 16.sp,
                modifier = alphaModifier
            )
            DateItem(date = communication.date, modifier = alphaModifier)
        }
        Text(text = if (communication.read) "Letta" else "Da leggere")
    }
}