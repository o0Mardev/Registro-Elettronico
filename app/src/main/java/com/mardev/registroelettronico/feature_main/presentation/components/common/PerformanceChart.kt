package com.mardev.registroelettronico.feature_main.presentation.components.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun PerformanceChart(modifier: Modifier = Modifier, list: List<Float>) {
    val lineColor = MaterialTheme.colorScheme.onPrimaryContainer
    val gridColor = Color.Gray.copy(alpha = 0.5f)

    Row(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onDraw = {
                // Draw grid lines
                val numberOfLines = 11 // From 0 to 10 inclusive
                val step = size.height / (numberOfLines - 1)
                for (i in 0 until numberOfLines) {
                    val y = size.height - i * step
                    drawLine(
                        color = gridColor,
                        start = Offset(x = 0f, y = y),
                        end = Offset(x = size.width, y = y),
                        strokeWidth = 1f
                    )
                }

                if (list.isNotEmpty()) {
                    if (list.size == 1){
                        val valuePercentage = getValuePercentageForRange(list.first())
                        val yPosition = size.height.times(1 - valuePercentage)
                        drawLine(
                            color = lineColor,
                            start = Offset(x = 0f, y = yPosition),
                            end = Offset(x = size.width, y = yPosition),
                            strokeWidth = 3f
                        )
                    }

                    val valuePercentageList = list.map { getValuePercentageForRange(it) }
                    val maxY = size.height
                    valuePercentageList.forEachIndexed { index, percentage ->
                        if (index < valuePercentageList.size - 1) {
                            val startX = index * (size.width / (valuePercentageList.size - 1))
                            val startY = maxY * (1 - valuePercentageList[index])
                            val endX = (index + 1) * (size.width / (valuePercentageList.size - 1))
                            val endY = maxY * (1 - valuePercentageList[index + 1])
                            drawLine(
                                color = lineColor,
                                start = Offset(x = startX, y = startY),
                                end = Offset(x = endX, y = endY),
                                strokeWidth = 3f
                            )
                        }
                    }
                }
            }
        )
    }
}


private fun getValuePercentageForRange(value: Float): Float {
    val min = 0f
    val max = 10f
    return (value - min) / (max - min)
}