package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
data class HomeScreenState(
    val date: Date = Date(),
    val events: DailyEvents = DailyEvents(emptyList(), emptyList(), emptyList(), emptyList()),
    val loading: Boolean = true
)
