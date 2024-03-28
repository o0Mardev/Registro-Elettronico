package com.mardev.registroelettronico.feature_main.presentation.components.absence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Alarm
import androidx.compose.material.icons.twotone.MenuBook
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence
import com.mardev.registroelettronico.feature_main.domain.model.TypeOfAbsence
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@Composable
fun AbsenceItem(
    genericAbsence: GenericAbsence,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
    showOverline: Boolean = false,
    showDivider: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        if (showIcon) {
            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .drawBehind {
                        drawCircle(
                            color = when (genericAbsence.typeOfAbsence) {
                                TypeOfAbsence.ABSENCE -> Color.Red.copy(alpha = 0.75f)
                                TypeOfAbsence.DELAY -> Color(
                                    red = 1f,
                                    green = 0.87f,
                                    blue = 0.34f,
                                    alpha = 0.75f
                                )

                                TypeOfAbsence.EXIT -> Color(
                                    red = 1f,
                                    green = 0.5f,
                                    blue = 0f,
                                    alpha = 0.75f
                                )

                                TypeOfAbsence.UNKNOWN -> Color.Unspecified
                            },
                            radius = this.size.maxDimension / 1.3f
                        )
                    },
                imageVector = Icons.TwoTone.Alarm,
                tint = Color.White,
                contentDescription = null
            )
        }
        Column(modifier = modifier) {

            if (showOverline) {
                Text(
                    text = when (genericAbsence.typeOfAbsence) {
                        TypeOfAbsence.ABSENCE -> "Assenza"
                        TypeOfAbsence.DELAY -> "Ritardo"
                        TypeOfAbsence.EXIT -> "Uscita"
                        TypeOfAbsence.UNKNOWN -> ""
                    }, style = MaterialTheme.typography.labelMedium
                )
            }

            if (genericAbsence.dateJustification == null) {
                Text(text = "Da giustificare")
            } else Text(text = "Data giustificazione: ${genericAbsence.dateJustification}")

            if (genericAbsence.reasonOfJustification.isNotBlank()) {
                Text(text = "Motivazione: ${genericAbsence.reasonOfJustification}")
            }
            if (genericAbsence.isCalculated) {
                Text(text = "Concorre al calcolo")
            } else {
                Text(text = "Non concorre al calcolo")
            }

            if (genericAbsence.classTime != null && !genericAbsence.time.isNullOrEmpty()) {
                when (genericAbsence.typeOfAbsence) {
                    TypeOfAbsence.DELAY -> Text(text = "Entra alle: ${genericAbsence.time}, ora di lezione: ${genericAbsence.classTime}")
                    TypeOfAbsence.EXIT -> Text(text = "Esce alle: ${genericAbsence.time}, ora di lezione: ${genericAbsence.classTime}")
                    else -> {}
                }
            }
        }
    }
    if (showDivider) {
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}