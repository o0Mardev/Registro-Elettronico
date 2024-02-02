package com.mardev.registroelettronico.feature_main.presentation.components.communication_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.sp
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.html.Html

@Composable
fun CommunicationItem(
    communication: Communication,
    modifier: Modifier = Modifier,
    onClick: (communicationId: Int) -> Unit,
) {
    val alphaModifier = if (communication.read) {
        modifier.alpha(ContentAlpha.medium)
    } else modifier

    var showMore by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = if (communication.attachments != null) modifier.clickable {
            if (!communication.read) {
                onClick(communication.id)
            }
            showMore = !showMore
        } else modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier
                .weight(1f)
        ) {
            Text(
                text = communication.title,
                fontSize = 16.sp,
                modifier = alphaModifier
            )
        }
        Text(text = if (communication.read) "Letta" else "Da leggere")
    }
    if (showMore) {
        if (communication.description.isNotBlank()) {
            Text(text = "Descrizione:")
            Html(html = communication.description)
        }
        if (communication.attachments != null) {
            Text(text = "Allegati:")
            Column {
                communication.attachments.forEach { attachment ->
                    val uriHandler = LocalUriHandler.current
                    Text(text = attachment.name ?: "", style = MaterialTheme.typography.bodySmall, modifier = modifier.clickable {
                        uriHandler.openUri(attachment.url)
                    })
                }
            }
        }
    }
}