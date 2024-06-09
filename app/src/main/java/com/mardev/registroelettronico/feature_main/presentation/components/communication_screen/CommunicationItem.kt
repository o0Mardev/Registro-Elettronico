package com.mardev.registroelettronico.feature_main.presentation.components.communication_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ireward.htmlcompose.HtmlText
import com.mardev.registroelettronico.feature_main.domain.model.Attachment
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import java.time.LocalDate

@Composable
fun CommunicationItem(
    modifier: Modifier = Modifier,
    communication: Communication,
    onClick: (communicationId: Int) -> Unit
) {

    val alphaModifier = if (communication.read) {
        modifier.alpha(0.45f)
    } else modifier

    var isExpanded by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(if (isExpanded) 180f else 0f)


    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                isExpanded = !isExpanded
                onClick(communication.id)
            }
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = modifier.weight(1f)) {
                    Text(
                        text = communication.title,
                        modifier = alphaModifier,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Text(
                    text = if (communication.read) {
                        "Letta"
                    } else "Da Leggere",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = alphaModifier
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                if (communication.description.isNotBlank()){
                    Text(
                        text = "Descrizione:",
                        modifier = alphaModifier,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    HtmlText(
                        text = communication.description,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = alphaModifier
                    )
                }

                if (!communication.attachments.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Link e allegati:",
                        modifier = alphaModifier,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    val uriHandler = LocalUriHandler.current
                    communication.attachments.forEach { attachment: Attachment ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = modifier
                                .defaultMinSize(minHeight = 24.dp)
                                .clickable { uriHandler.openUri(attachment.url) }
                        ) {
                            when (attachment.type) {
                                "0" -> {
                                    Icon(
                                        imageVector = Icons.Default.Link,
                                        modifier = alphaModifier,
                                        contentDescription = null
                                    )
                                    Text(
                                        text = attachment.url,
                                        modifier = alphaModifier,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                "1" -> {
                                    Icon(
                                        imageVector = Icons.Default.Attachment,
                                        modifier = alphaModifier,
                                        contentDescription = null
                                    )
                                    Text(
                                        text = attachment.name ?: "",
                                        modifier = alphaModifier,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommunicationItem() {
    CommunicationItem(
        communication = Communication(
            id = 0,
            studentId = 1,
            title = "Circolare numero 317",
            description = "Si allega la circolare numero 317",
            date = LocalDate.now(),
            read = false,
            attachments = null
        )
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommunicationItem1() {
    CommunicationItem(
        communication = Communication(
            id = 0,
            studentId = 1,
            title = "Circolare numero 317",
            description = "Si allega la circolare numero 317",
            date = LocalDate.now(),
            read = true,
            attachments = null
        )
    ) {

    }
}